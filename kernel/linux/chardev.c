#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <asm/uaccess.h>

/*Dev name as it appears in /proc/devices   */
#define DEVICE_NAME "yydev"
#define BUF_LEN     80
#define SUCCESS     0

int init_module(void);
void cleanup_module(void);
static int device_open(struct inode *inode, struct file *file);
static int device_release(struct inode *inode, struct file *file);
static ssize_t device_read(struct file *filp, char *buf,
    size_t length, loff_t *offset);
static ssize_t device_write(struct file *filp, const char *buf,
    size_t len, loff_t *off);

static int major = 0;
static int device_opened = 0; 
static char msg[BUF_LEN] = {'\0'};
static char *msg_ptr = NULL;

static struct file_operations fops = {
    .read = device_read,
    .write = device_write,
    .open = device_open,
    .release = device_release
};

int init_module(void)
{
    major = register_chrdev(0, DEVICE_NAME, &fops);

    if (major < 0) {
        printk(KERN_ALERT "Registering char device failed with %d\n", major);
        return major;
    }

    printk(KERN_INFO "I was assigned major number %d. To talk to\n", major);
    printk(KERN_INFO "the driver, create a dev file with\n");
    printk(KERN_INFO "'mknod /dev/%s c %d 0'.\n", DEVICE_NAME, major);
    printk(KERN_INFO "Try various minor numbers. Try to cat and echo to\n");
    printk(KERN_INFO "the device file.\n");
    printk(KERN_INFO "Remove the device file and module when done.\n");
    return SUCCESS;
}

void cleanup_module(void)
{
    int ret = unregister_chrdev(major, DEVICE_NAME);

    if (ret < 0) {
        printk(KERN_ALERT "Error in unregister_chrdev: %d\n", ret);
    }
}

/*
Called when a process tries to open the device file, like
"cat /dev/mycharfile"
*/
static int device_open(struct inode *inode, struct file *file)
{
    static int counter = 0;
    
    if (0 != device_opened) {
        return -EBUSY;
    }
    
    device_opened++;
    sprintf(msg, "I already told you %d times Hello world!\n", counter++);
    msg_ptr = msg;
    try_module_get(THIS_MODULE);
    return SUCCESS;
}

/*Called when a process closes the device file. */
static int device_release(struct inode *inode, struct file *file)
{
    device_opened--;
    module_put(THIS_MODULE);
    return 0;
}

/*
Called when a process, which already opened the dev file, attempts to
read from it.
*/
static ssize_t device_read(struct file *filp, char *buffer,
    size_t length, loff_t *offset)
{
    int bytes_read = 0;
    
    /*
     *If we're at the end of the message, 
     *return 0 signifying end of file 
     */
    if (0 == *msg_ptr) {
            return 0;
    }
    
    /*Actually put the data into the buffer */
    while (0!=length && 0!=*msg_ptr) {
       /*
        *The buffer is in the user data segment, not the kernel 
        *segment so "*" assignment won't work.  We have to use 
        *put_user which copies data from the kernel data segment to
        *the user data segment. 
        */
       put_user(*(msg_ptr++), buffer++);
       length--;
       bytes_read++;
    }
    
    /*
     *Most read functions return the number of bytes put into the buffer
     */
    return bytes_read;
}

/* 
 *Called when a process writes to dev file: echo "hi" > /dev/hello 
 */
static ssize_t device_write(struct file *filp, const char *buff,
    size_t len, loff_t *off)
{
    printk(KERN_ALERT "Sorry, this operation isn't supported.\n");
    return -EINVAL;
}
