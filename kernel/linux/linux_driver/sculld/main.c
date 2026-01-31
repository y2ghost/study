#include "sculld.h"     /* local definitions */
#include <linux/autoconf.h>
#include <linux/module.h>
#include <linux/moduleparam.h>
#include <linux/init.h>
#include <linux/kernel.h>   /* printk() */
#include <linux/slab.h>     /* kmalloc() */
#include <linux/fcntl.h>    /* O_ACCMODE */
#include <linux/aio.h>
#include <asm/uaccess.h>

int sculld_major = SCULLD_MAJOR;
int sculld_devs = SCULLD_DEVS;   /* number of bare sculld devices */
int sculld_qset = SCULLD_QSET;
int sculld_order = SCULLD_ORDER;

module_param(sculld_major, int, 0);
module_param(sculld_devs, int, 0);
module_param(sculld_qset, int, 0);
module_param(sculld_order, int, 0);
MODULE_AUTHOR("Alessandro Rubini");
MODULE_LICENSE("GPL");

struct sculld_dev *sculld_devices = NULL; /* allocated in sculld_init */

int sculld_trim(struct sculld_dev *dev);
void sculld_cleanup(void);

/* Device model stuff */
static struct ldd_driver sculld_driver = {
    .version = "yy version",
    .module = THIS_MODULE,
    .driver = {
        .name = "sculld",
    },
};

#ifdef SCULLD_USE_PROC /* don't waste space if unused */
/*
 * The proc filesystem: function to read and entry
 */
void sculld_proc_offset(char *buf, char **start, off_t *offset, int *len)
{
    if (0 == *offset) {
        return;
    }

    if (*offset >= *len) {
        /* Not there yet */
        *offset -= *len;
        *len = 0;
    } else {
        /* We're into the interesting stuff now */
        *start = buf + *offset;
        *offset = 0;
    }
}

/* FIXME: Do we need this here??  It be ugly  */
int sculld_read_procmem(char *buf, char **start, off_t offset,
    int count, int *eof, void *data)
{
    int i = 0;
    int j = 0;
    int order = 0;
    int qset = 0;
    int len = 0;
    int limit = count - 80; /* Don't print more than this */
    struct sculld_dev *d = NULL;

    *start = buf;
    for (i=0; i<sculld_devs; i++) {
        d = &sculld_devices[i];
        if (down_interruptible (&d->sem)) {
            return -ERESTARTSYS;
        }

        qset = d->qset;  /* retrieve the features of each device */
        order = d->order;
        len += sprintf(buf+len,"\nDevice %i: qset %i, order %i, sz %li\n",
            i, qset, order, (long)(d->size));

        for (; NULL!=d; d=d->next) { /* scan the list */
            len += sprintf(buf+len,"  item at %p, qset at %p\n",d,d->data);
            sculld_proc_offset (buf, start, &offset, &len);

            if (len > limit) {
                goto out;
            }

            /* dump only the last item - save space */
            if (NULL!=d->data && NULL==d->next) {
                for (j=0; j<qset; j++) {
                    if (NULL != d->data[j]) {
                        len += sprintf(buf+len,"    % 4i:%8p\n",j,d->data[j]);
                    }

                    sculld_proc_offset (buf, start, &offset, &len);
                    if (len > limit) {
                        goto out;
                    }
                }
            }
        }

out:
        up (&sculld_devices[i].sem);
        if (len > limit) {
            break;
        }
    }

    *eof = 1;
    return len;
}
#endif /* SCULLD_USE_PROC */

/*
 * Open and close
 */
int sculld_open(struct inode *inode, struct file *filp)
{
    struct sculld_dev *dev = NULL; /* device information */

    /*  Find the device */
    dev = container_of(inode->i_cdev, struct sculld_dev, cdev);
    /* now trim to 0 the length of the device if open was write-only */
    if (O_WRONLY == (filp->f_flags & O_ACCMODE)) {
        if (down_interruptible (&dev->sem)) {
            return -ERESTARTSYS;
        }

        sculld_trim(dev); /* ignore errors */
        up (&dev->sem);
    }

    /* and use filp->private_data to point to the device data */
    filp->private_data = dev;
    return 0;          /* success */
}

int sculld_release (struct inode *inode, struct file *filp)
{
    return 0;
}

/*
 * Follow the list 
 */
struct sculld_dev *sculld_follow(struct sculld_dev *dev, int n)
{
    while (n--) {
        if (NULL == dev->next) {
            dev->next = kmalloc(sizeof(struct sculld_dev), GFP_KERNEL);
            memset(dev->next, 0, sizeof(struct sculld_dev));
        }

        dev = dev->next;
        continue;
    }

    return dev;
}

/*
 * Data management: read and write
 */
ssize_t sculld_read (struct file *filp, char __user *buf,
    size_t count, loff_t *f_pos)
{
    struct sculld_dev *dev = filp->private_data; /* the first listitem */
    struct sculld_dev *dptr = NULL;
    int quantum = PAGE_SIZE << dev->order;
    int qset = dev->qset;
    int itemsize = quantum * qset; /* how many bytes in the listitem */
    int item = 0;
    int s_pos = 0;
    int q_pos = 0;
    int rest = 0;
    ssize_t retval = 0;

    if (down_interruptible (&dev->sem)) {
        return -ERESTARTSYS;
    }

    if (*f_pos > dev->size) {
        goto nothing;
    }

    if (*f_pos + count > dev->size) {
        count = dev->size - *f_pos;
    }

    /* find listitem, qset index, and offset in the quantum */
    item = ((long) *f_pos) / itemsize;
    rest = ((long) *f_pos) % itemsize;
    s_pos = rest / quantum; q_pos = rest % quantum;
    /* follow the list up to the right position (defined elsewhere) */
    dptr = sculld_follow(dev, item);

    if (NULL == dptr->data) {
        goto nothing; /* don't fill holes */
    }

    if (NULL == dptr->data[s_pos]) {
        goto nothing;
    }

    if (count > quantum - q_pos) {
        count = quantum - q_pos; /* read only up to the end of this quantum */
    }

    if (copy_to_user (buf, dptr->data[s_pos]+q_pos, count)) {
        retval = -EFAULT;
        goto nothing;
    }

    up (&dev->sem);
    *f_pos += count;
    return count;

nothing:
    up (&dev->sem);
    return retval;
}

ssize_t sculld_write (struct file *filp, const char __user *buf,
    size_t count, loff_t *f_pos)
{
    struct sculld_dev *dev = filp->private_data;
    struct sculld_dev *dptr = NULL;
    int quantum = PAGE_SIZE << dev->order;
    int qset = dev->qset;
    int itemsize = quantum * qset;
    int item = 0;
    int s_pos = 0;
    int q_pos = 0;
    int rest = 0;
    ssize_t retval = -ENOMEM; /* our most likely error */

    if (down_interruptible (&dev->sem)) {
        return -ERESTARTSYS;
    }

    /* find listitem, qset index and offset in the quantum */
    item = ((long) *f_pos) / itemsize;
    rest = ((long) *f_pos) % itemsize;
    s_pos = rest / quantum; q_pos = rest % quantum;

    /* follow the list up to the right position */
    dptr = sculld_follow(dev, item);
    if (NULL == dptr->data) {
        dptr->data = kmalloc(qset * sizeof(void *), GFP_KERNEL);
        if (NULL == dptr->data) {
            goto nomem;
        }

        memset(dptr->data, 0, qset * sizeof(char *));
    }

    /* Here's the allocation of a single quantum */
    if (NULL == dptr->data[s_pos]) {
        dptr->data[s_pos] = (void *)__get_free_pages(GFP_KERNEL, dptr->order);
        if (NULL == dptr->data[s_pos]) {
            goto nomem;
        }

        memset(dptr->data[s_pos], 0, PAGE_SIZE << dptr->order);
    }

    if (count > quantum - q_pos) {
        count = quantum - q_pos; /* write only up to the end of this quantum */
    }

    if (copy_from_user(dptr->data[s_pos]+q_pos, buf, count)) {
        retval = -EFAULT;
        goto nomem;
    }

    *f_pos += count;
    /* update the size */
    if (dev->size < *f_pos) {
        dev->size = *f_pos;
    }

    up (&dev->sem);
    return count;

nomem:
    up (&dev->sem);
    return retval;
}

/*
 * The ioctl() implementation
 */
int sculld_ioctl (struct inode *inode, struct file *filp,
    unsigned int cmd, unsigned long arg)
{
    int err = 0;
    int ret = 0;
    int tmp = 0;

    /* don't even decode wrong cmds: better returning  ENOTTY than EFAULT */
    if (_IOC_TYPE(cmd) != SCULLD_IOC_MAGIC) {
        return -ENOTTY;
    }

    if (_IOC_NR(cmd) > SCULLD_IOC_MAXNR) {
        return -ENOTTY;
    }

    /*
     * the type is a bitmask, and VERIFY_WRITE catches R/W
     * transfers. Note that the type is user-oriented, while
     * verify_area is kernel-oriented, so the concept of "read" and
     * "write" is reversed
     */
    if (_IOC_DIR(cmd) & _IOC_READ) {
        err = !access_ok(VERIFY_WRITE, (void __user *)arg, _IOC_SIZE(cmd));
    } else if (_IOC_DIR(cmd) & _IOC_WRITE) {
        err =  !access_ok(VERIFY_READ, (void __user *)arg, _IOC_SIZE(cmd));
    }

    if (0 != err) {
        return -EFAULT;
    }

    switch(cmd) {
    case SCULLD_IOCRESET:
        sculld_qset = SCULLD_QSET;
        sculld_order = SCULLD_ORDER;
        break;
    case SCULLD_IOCSORDER: /* Set: arg points to the value */
        ret = __get_user(sculld_order, (int __user *) arg);
        break;
    case SCULLD_IOCTORDER: /* Tell: arg is the value */
        sculld_order = arg;
        break;
    case SCULLD_IOCGORDER: /* Get: arg is pointer to result */
        ret = __put_user (sculld_order, (int __user *) arg);
        break;
    case SCULLD_IOCQORDER: /* Query: return it (it's positive) */
        return sculld_order;
    case SCULLD_IOCXORDER: /* eXchange: use arg as pointer */
        tmp = sculld_order;
        ret = __get_user(sculld_order, (int __user *) arg);

        if (0 == ret) {
            ret = __put_user(tmp, (int __user *) arg);
        }

        break;
    case SCULLD_IOCHORDER: /* sHift: like Tell + Query */
        tmp = sculld_order;
        sculld_order = arg;
        return tmp;
    case SCULLD_IOCSQSET:
        ret = __get_user(sculld_qset, (int __user *) arg);
        break;
    case SCULLD_IOCTQSET:
        sculld_qset = arg;
        break;
    case SCULLD_IOCGQSET:
        ret = __put_user(sculld_qset, (int __user *)arg);
        break;
    case SCULLD_IOCQQSET:
        return sculld_qset;
    case SCULLD_IOCXQSET:
        tmp = sculld_qset;
        ret = __get_user(sculld_qset, (int __user *)arg);

        if (0 == ret) {
            ret = __put_user(tmp, (int __user *)arg);
        }

        break;
    case SCULLD_IOCHQSET:
        tmp = sculld_qset;
        sculld_qset = arg;
        return tmp;
    default:  /* redundant, as cmd was checked against MAXNR */
        return -ENOTTY;
    }

    return ret;
}

/*
 * The "extended" operations
 */
loff_t sculld_llseek (struct file *filp, loff_t off, int whence)
{
    struct sculld_dev *dev = filp->private_data;
    long newpos = 0;

    switch(whence) {
    case 0: /* SEEK_SET */
        newpos = off;
        break;
    case 1: /* SEEK_CUR */
        newpos = filp->f_pos + off;
        break;
    case 2: /* SEEK_END */
        newpos = dev->size + off;
        break;
    default: /* can't happen */
        return -EINVAL;
    }

    if (newpos < 0) {
        return -EINVAL;
    }

    filp->f_pos = newpos;
    return newpos;
}

/*
 * A simple asynchronous I/O implementation.
 */
struct async_work {
    struct kiocb *iocb;
    int result;
    struct work_struct work;
};

/*
 * "Complete" an asynchronous operation.
 */
static void sculld_do_deferred_op(void *p)
{
    struct async_work *stuff = (struct async_work *) p;

    aio_complete(stuff->iocb, stuff->result, 0);
    kfree(stuff);
}

static int sculld_defer_op(int write, struct kiocb *iocb, char __user *buf,
    size_t count, loff_t pos)
{
    struct async_work *stuff = NULL;
    int result = 0;

    /* Copy now while we can access the buffer */
    if (0 != write) {
        result = sculld_write(iocb->ki_filp, buf, count, &pos);
    } else {
        result = sculld_read(iocb->ki_filp, buf, count, &pos);
    }

    /* If this is a synchronous IOCB, we return our status now. */
    if (is_sync_kiocb(iocb)) {
        return result;
    }

    /* Otherwise defer the completion for a few milliseconds. */
    stuff = kmalloc (sizeof (*stuff), GFP_KERNEL);
    if (NULL == stuff) {
        return result; /* No memory, just complete now */
    }

    stuff->iocb = iocb;
    stuff->result = result;
    INIT_WORK(&stuff->work, sculld_do_deferred_op, stuff);
    schedule_delayed_work(&stuff->work, HZ/100);
    return -EIOCBQUEUED;
}

static ssize_t sculld_aio_read(struct kiocb *iocb, char __user *buf,
    size_t count, loff_t pos)
{
    return sculld_defer_op(0, iocb, buf, count, pos);
}

static ssize_t sculld_aio_write(struct kiocb *iocb, const char __user *buf,
    size_t count, loff_t pos)
{
    return sculld_defer_op(1, iocb, (char __user *) buf, count, pos);
}

/*
 * Mmap *is* available, but confined in a different file
 */
extern int sculld_mmap(struct file *filp, struct vm_area_struct *vma);

/*
 * The fops
 */
struct file_operations sculld_fops = {
    .owner = THIS_MODULE,
    .llseek = sculld_llseek,
    .read = sculld_read,
    .write = sculld_write,
    .ioctl = sculld_ioctl,
    .mmap = sculld_mmap,
    .open = sculld_open,
    .release = sculld_release,
    .aio_read = sculld_aio_read,
    .aio_write = sculld_aio_write,
};

int sculld_trim(struct sculld_dev *dev)
{
    struct sculld_dev *next = NULL;
    struct sculld_dev *dptr = NULL;
    int qset = dev->qset;   /* "dev" is not-null */
    int i = 0;

    if (dev->vmas) {
        /* don't trim: there are active mappings */
        return -EBUSY;
    }

    for (dptr=dev; NULL!=dptr; dptr=next) { /* all the list items */
        if (NULL != dptr->data) {
            /* This code frees a whole quantum-set */
            for (i=0; i<qset; i++) {
                if (NULL != dptr->data[i]) {
                    free_pages((unsigned long)(dptr->data[i]), dptr->order);
                }
            }

            kfree(dptr->data);
            dptr->data = NULL;
        }

        next = dptr->next;
        if (dptr != dev) {
            kfree(dptr); /* all of them but the first */
        }
    }

    dev->size = 0;
    dev->qset = sculld_qset;
    dev->order = sculld_order;
    dev->next = NULL;
    return 0;
}

static void sculld_setup_cdev(struct sculld_dev *dev, int index)
{
    int err = 0;
    int devno = MKDEV(sculld_major, index);
    
    cdev_init(&dev->cdev, &sculld_fops);
    dev->cdev.owner = THIS_MODULE;
    dev->cdev.ops = &sculld_fops;

    /* Fail gracefully if need be */
    err = cdev_add (&dev->cdev, devno, 1);
    if (0 != err) {
        printk(KERN_NOTICE "Error %d adding scull%d", err, index);
    }
}

static ssize_t sculld_show_dev(struct device *ddev,
    struct device_attribute *attr, char *buf)
{
    struct sculld_dev *dev = ddev->driver_data;

    return print_dev_t(buf, dev->cdev.dev);
}

static DEVICE_ATTR(dev, S_IRUGO, sculld_show_dev, NULL);

static void sculld_register_dev(struct sculld_dev *dev, int index)
{
    sprintf(dev->devname, "sculld%d", index);
    dev->ldev.name = dev->devname;
    dev->ldev.driver = &sculld_driver;
    dev->ldev.dev.driver_data = dev;
    register_ldd_device(&dev->ldev);
    device_create_file(&dev->ldev.dev, &dev_attr_dev);
}

/*
 * Finally, the module stuff
 */
int sculld_init(void)
{
    int result = 0;
    int i = 0;
    dev_t dev = MKDEV(sculld_major, 0);
    
    /*
     * Register your major, and accept a dynamic number.
     */
    if (0 != sculld_major) {
        result = register_chrdev_region(dev, sculld_devs, "sculld");
    } else {
        result = alloc_chrdev_region(&dev, 0, sculld_devs, "sculld");
        sculld_major = MAJOR(dev);
    }

    if (result < 0) {
        return result;
    }

    /*
     * Register with the driver core.
     */
    register_ldd_driver(&sculld_driver);
    /* 
     * allocate the devices -- we can't have them static, as the number
     * can be specified at load time
     */
    sculld_devices = kmalloc(sculld_devs*sizeof (struct sculld_dev), GFP_KERNEL);

    if (NULL == sculld_devices) {
        result = -ENOMEM;
        goto fail_malloc;
    }

    memset(sculld_devices, 0, sculld_devs*sizeof (struct sculld_dev));
    for (i=0; i<sculld_devs; i++) {
        sculld_devices[i].order = sculld_order;
        sculld_devices[i].qset = sculld_qset;
        sema_init (&sculld_devices[i].sem, 1);
        sculld_setup_cdev(sculld_devices + i, i);
        sculld_register_dev(sculld_devices + i, i);
    }

#ifdef SCULLD_USE_PROC /* only when available */
    create_proc_read_entry("sculldmem", 0, NULL, sculld_read_procmem, NULL);
#endif
    return 0; /* succeed */

fail_malloc:
    unregister_chrdev_region(dev, sculld_devs);
    return result;
}

void sculld_cleanup(void)
{
    int i = 0;

#ifdef SCULLD_USE_PROC
    remove_proc_entry("sculldmem", NULL);
#endif
    for (i=0; i<sculld_devs; i++) {
        unregister_ldd_device(&sculld_devices[i].ldev);
        cdev_del(&sculld_devices[i].cdev);
        sculld_trim(sculld_devices + i);
    }

    kfree(sculld_devices);
    unregister_ldd_driver(&sculld_driver);
    unregister_chrdev_region(MKDEV (sculld_major, 0), sculld_devs);
}

module_init(sculld_init);
module_exit(sculld_cleanup);
