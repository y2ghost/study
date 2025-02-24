#include "scull.h"        /* local definitions */
#include <linux/module.h>
#include <linux/kernel.h> /* printk() */
#include <linux/slab.h>   /* kmalloc() */
#include <linux/fcntl.h>
#include <linux/tty.h>
#include <asm/atomic.h>
#include <linux/list.h>

static dev_t scull_a_firstdev;
static struct scull_dev scull_s_device;
static atomic_t scull_s_available = ATOMIC_INIT(1);

static int scull_s_open(struct inode *inode, struct file *filp)
{
    struct scull_dev *dev = &scull_s_device; /* device information */

    if (0 == atomic_dec_and_test (&scull_s_available)) {
        atomic_inc(&scull_s_available);
        return -EBUSY; /* already open */
    }

    /* then, everything else is copied from the bare scull device */
    if (O_WRONLY == (filp->f_flags & O_ACCMODE)) {
        scull_trim(dev);
    }

    filp->private_data = dev;
    return 0;          /* success */
}

static int scull_s_release(struct inode *inode, struct file *filp)
{
    atomic_inc(&scull_s_available); /* release the device */
    return 0;
}

/*
 * The other operations for the single-open device come from the bare device
 */
static struct file_operations scull_sngl_fops = {
    .owner = THIS_MODULE,
    .llseek = scull_llseek,
    .read = scull_read,
    .write = scull_write,
    .ioctl = scull_ioctl,
    .open = scull_s_open,
    .release = scull_s_release,
};

/************************************************************************
 *
 * Next, the "uid" device. It can be opened multiple times by the
 * same user, but access is denied to other users if the device is open
 */
static struct scull_dev scull_u_device;
static int scull_u_count = 0;   /* initialized to 0 by default */
static uid_t scull_u_owner = 0; /* initialized to 0 by default */
static spinlock_t scull_u_lock = SPIN_LOCK_UNLOCKED;

static int scull_u_open(struct inode *inode, struct file *filp)
{
    struct scull_dev *dev = &scull_u_device; /* device information */

    spin_lock(&scull_u_lock);
    if (0!=scull_u_count && 
        (scull_u_owner!=current->uid) &&  /* allow user */
        (scull_u_owner!=current->euid) && /* allow whoever did su */
        0==capable(CAP_DAC_OVERRIDE)) { /* still allow root */
        spin_unlock(&scull_u_lock);
        return -EBUSY;   /* -EPERM would confuse the user */
    }

    if (0 == scull_u_count) {
        scull_u_owner = current->uid; /* grab it */
    }

    scull_u_count++;
    spin_unlock(&scull_u_lock);

    /* then, everything else is copied from the bare scull device */
    if (O_WRONLY == (filp->f_flags & O_ACCMODE)) {
        scull_trim(dev);
    }

    filp->private_data = dev;
    return 0;          /* success */
}

static int scull_u_release(struct inode *inode, struct file *filp)
{
    spin_lock(&scull_u_lock);
    scull_u_count--; /* nothing else */
    spin_unlock(&scull_u_lock);
    return 0;
}

/*
 * The other operations for the device come from the bare device
 */
static struct file_operations scull_user_fops = {
    .owner = THIS_MODULE,
    .llseek = scull_llseek,
    .read = scull_read,
    .write = scull_write,
    .ioctl = scull_ioctl,
    .open = scull_u_open,
    .release = scull_u_release,
};

/************************************************************************
 *
 * Next, the device with blocking-open based on uid
 */
static struct scull_dev scull_w_device;
static int scull_w_count = 0;   /* initialized to 0 by default */
static uid_t scull_w_owner = 0; /* initialized to 0 by default */
static DECLARE_WAIT_QUEUE_HEAD(scull_w_wait);
static spinlock_t scull_w_lock = SPIN_LOCK_UNLOCKED;

static inline int scull_w_available(void)
{
    return scull_w_count == 0 ||
        scull_w_owner == current->uid ||
        scull_w_owner == current->euid ||
        capable(CAP_DAC_OVERRIDE);
}

static int scull_w_open(struct inode *inode, struct file *filp)
{
    struct scull_dev *dev = &scull_w_device; /* device information */

    spin_lock(&scull_w_lock);
    while (0 == scull_w_available()) {
        spin_unlock(&scull_w_lock);
        if (filp->f_flags & O_NONBLOCK) {
            return -EAGAIN;
        }

        if (wait_event_interruptible(scull_w_wait, scull_w_available())) {
            return -ERESTARTSYS; /* tell the fs layer to handle it */
        }

        spin_lock(&scull_w_lock);
    }

    if (0 == scull_w_count) {
        scull_w_owner = current->uid; /* grab it */
    }

    scull_w_count++;
    spin_unlock(&scull_w_lock);

    /* then, everything else is copied from the bare scull device */
    if (O_WRONLY == (filp->f_flags & O_ACCMODE)) {
        scull_trim(dev);
    }

    filp->private_data = dev;
    return 0;          /* success */
}

static int scull_w_release(struct inode *inode, struct file *filp)
{
    int temp = 0;

    spin_lock(&scull_w_lock);
    scull_w_count--;
    temp = scull_w_count;
    spin_unlock(&scull_w_lock);

    if (0 == temp) {
        wake_up_interruptible_sync(&scull_w_wait); /* awake other uid's */
    }

    return 0;
}

/*
 * The other operations for the device come from the bare device
 */
struct file_operations scull_wusr_fops = {
    .owner = THIS_MODULE,
    .llseek = scull_llseek,
    .read = scull_read,
    .write = scull_write,
    .ioctl = scull_ioctl,
    .open = scull_w_open,
    .release = scull_w_release,
};

/************************************************************************
 *
 * Finally the `cloned' private device. This is trickier because it
 * involves list management, and dynamic allocation.
 */
struct scull_listitem {
    struct scull_dev device;
    dev_t key;
    struct list_head list;
};

/* The list of devices, and a lock to protect it */
static LIST_HEAD(scull_c_list);
static spinlock_t scull_c_lock = SPIN_LOCK_UNLOCKED;
/* A placeholder scull_dev which really just holds the cdev stuff. */
static struct scull_dev scull_c_device;   

/* Look for a device or create one if missing */
static struct scull_dev *scull_c_lookfor_device(dev_t key)
{
    struct scull_listitem *lptr = NULL;

    list_for_each_entry(lptr, &scull_c_list, list) {
        if (lptr->key == key) {
            return &(lptr->device);
        }
    }

    /* not found */
    lptr = kmalloc(sizeof(struct scull_listitem), GFP_KERNEL);
    if (NULL == lptr) {
        return NULL;
    }

    /* initialize the device */
    memset(lptr, 0x0, sizeof(struct scull_listitem));
    lptr->key = key;
    scull_trim(&(lptr->device)); /* initialize it */
    init_MUTEX(&(lptr->device.sem));
    /* place it in the list */
    list_add(&lptr->list, &scull_c_list);
    return &(lptr->device);
}

static int scull_c_open(struct inode *inode, struct file *filp)
{
    struct scull_dev *dev = NULL;
    dev_t key;
 
    if (NULL == current->signal->tty) { 
        PDEBUG("Process \"%s\" has no ctl tty\n", current->comm);
        return -EINVAL;
    }

    key = tty_devnum(current->signal->tty);
    /* look for a scullc device in the list */
    spin_lock(&scull_c_lock);
    dev = scull_c_lookfor_device(key);
    spin_unlock(&scull_c_lock);

    if (NULL == dev) {
        return -ENOMEM;
    }

    /* then, everything else is copied from the bare scull device */
    if (O_WRONLY == (filp->f_flags & O_ACCMODE)) {
        scull_trim(dev);
    }

    filp->private_data = dev;
    return 0;          /* success */
}

static int scull_c_release(struct inode *inode, struct file *filp)
{
    /*
     * Nothing to do, because the device is persistent.
     * A `real' cloned device should be freed on last close
     */
    return 0;
}

/*
 * The other operations for the device come from the bare device
 */
struct file_operations scull_priv_fops = {
    .owner = THIS_MODULE,
    .llseek = scull_llseek,
    .read = scull_read,
    .write = scull_write,
    .ioctl = scull_ioctl,
    .open = scull_c_open,
    .release = scull_c_release,
};

/************************************************************************
 *
 * And the init and cleanup functions come last
 */
static struct scull_adev_info {
    char *name;
    struct scull_dev *sculldev;
    struct file_operations *fops;
} scull_access_devs[] = {
    { "scullsingle", &scull_s_device, &scull_sngl_fops },
    { "sculluid", &scull_u_device, &scull_user_fops },
    { "scullwuid", &scull_w_device, &scull_wusr_fops },
    { "sullpriv", &scull_c_device, &scull_priv_fops }
};

#define SCULL_N_ADEVS 4

static void scull_access_setup(dev_t devno, struct scull_adev_info *devinfo)
{
    int err = 0;
    struct scull_dev *dev = devinfo->sculldev;

    /* Initialize the device structure */
    dev->quantum = scull_quantum;
    dev->qset = scull_qset;
    init_MUTEX(&dev->sem);

    /* Do the cdev stuff. */
    cdev_init(&dev->cdev, devinfo->fops);
    kobject_set_name(&dev->cdev.kobj, devinfo->name);
    dev->cdev.owner = THIS_MODULE;
    err = cdev_add (&dev->cdev, devno, 1);
    
    /* Fail gracefully if need be */
    if (0 != err) {
        printk(KERN_NOTICE "Error %d adding %s\n", err, devinfo->name);
        kobject_put(&dev->cdev.kobj);
    } else {
        printk(KERN_NOTICE "%s registered at %x\n", devinfo->name, devno);
    }
}

int scull_access_init(dev_t firstdev)
{
    int i = 0;
    int result = 0;

    /* Get our number space */
    result = register_chrdev_region (firstdev, SCULL_N_ADEVS, "sculla");
    if (result < 0) {
        printk(KERN_WARNING "sculla: device number registration failed\n");
        return 0;
    }

    scull_a_firstdev = firstdev;
    /* Set up each device. */
    for (i=0; i<SCULL_N_ADEVS; i++) {
        scull_access_setup(firstdev+i, scull_access_devs+i);
    }

    return SCULL_N_ADEVS;
}

/*
 * This is called by cleanup_module or on failure.
 * It is required to never fail, even if nothing was initialized first
 */
void scull_access_cleanup(void)
{
    int i = 0;
    struct scull_listitem *lptr = NULL;
    struct scull_listitem *next = NULL;

    /* Clean up the static devs */
    for (i=0; i<SCULL_N_ADEVS; i++) {
        struct scull_dev *dev = scull_access_devs[i].sculldev;

        cdev_del(&dev->cdev);
        scull_trim(scull_access_devs[i].sculldev);
    }

        /* And all the cloned devices */
    list_for_each_entry_safe(lptr, next, &scull_c_list, list) {
        list_del(&lptr->list);
        scull_trim(&(lptr->device));
        kfree(lptr);
    }

    /* Free up our number space */
    unregister_chrdev_region(scull_a_firstdev, SCULL_N_ADEVS);
    return;
}
