#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/proc_fs.h>
#include <asm/uaccess.h>

#define PROC_ENTRY_FILENAME "buffer2k"
#define PROCFS_MAX_SIZE     2048

static char procfs_buffer[PROCFS_MAX_SIZE] = {'\0'};
static unsigned long procfs_buffer_size = 0;
static struct proc_dir_entry *Our_Proc_File = NULL;

static ssize_t procfs_read(struct file *filp, char *buffer,  
    size_t length, loff_t * offset)
{
    static int finished = 0;

    /* 
     * We return 0 to indicate end of file, that we have
     * no more information. Otherwise, processes will
     * continue to read from us in an endless loop. 
     */
    if (0 !=finished) {
            printk(KERN_INFO "procfs_read: END\n");
            finished = 0;
            return 0;
    }
    
    finished = 1;
    /* 
     * We use put_to_user to copy the string from the kernel's
     * memory segment to the memory segment of the process
     * that called us. get_from_user, BTW, is
     * used for the reverse. 
     */
    if (copy_to_user(buffer, procfs_buffer, procfs_buffer_size)) {
            return -EFAULT;
    }
    
    printk(KERN_INFO "procfs_read: read %lu bytes\n", procfs_buffer_size);
    return procfs_buffer_size;      /* Return the number of bytes "read" */
}

/*
 * This function is called when /proc is written
 */
static ssize_t
procfs_write(struct file *file, const char *buffer, size_t len, loff_t * off)
{
    if (len > PROCFS_MAX_SIZE) {
        procfs_buffer_size = PROCFS_MAX_SIZE;
    } else {
        procfs_buffer_size = len;
    }
    
    if (0 != copy_from_user(procfs_buffer, buffer, procfs_buffer_size)) {
        return -EFAULT;
    }
    
    printk(KERN_INFO "procfs_write: write %lu bytes\n", procfs_buffer_size);
    return procfs_buffer_size;
}

/* 
 * This function decides whether to allow an operation
 * (return zero) or not allow it (return a non-zero
 * which indicates why it is not allowed).
 *
 * The operation can be one of the following values:
 * 0 - Execute (run the "file" - meaningless in our case)
 * 2 - Write (input to the kernel module)
 * 4 - Read (output from the kernel module)
 *
 * This is the real function that checks file
 * permissions. The permissions returned by ls -l are
 * for referece only, and can be overridden here.
 */

static int module_permission(struct inode *inode, int op, struct nameidata *foo)
{
    /* 
     * We allow everybody to read from our module, but
     * only root (uid 0) may write to it 
     */
    if (4==op || (2==op && 0==current->euid)) {
        return 0;
    }
    
    /* 
     * If it's anything else, access is denied 
     */
    return -EACCES;
}

/* 
 * The file is opened - we don't really care about
 * that, but it does mean we need to increment the
 * module's reference count. 
 */
int procfs_open(struct inode *inode, struct file *file)
{
    try_module_get(THIS_MODULE);
    return 0;
}

/* 
 * The file is closed - again, interesting only because
 * of the reference count. 
 */
int procfs_close(struct inode *inode, struct file *file)
{
    module_put(THIS_MODULE);
    return 0;
}

static struct file_operations File_Ops_4_Our_Proc_File = {
    .read    = procfs_read,
    .write   = procfs_write,
    .open    = procfs_open,
    .release = procfs_close,
};

/* 
 * Inode operations for our proc file. We need it so
 * we'll have some place to specify the file operations
 * structure we want to use, and the function we use for
 * permissions. It's also possible to specify functions
 * to be called for anything else which could be done to
 * an inode (although we don't bother, we just put
 * NULL). 
 */

static struct inode_operations Inode_Ops_4_Our_Proc_File = {
    .permission = module_permission,        /* check for permissions */
};

/* 
 * Module initialization and cleanup 
 */
int init_module()
{
    /* create the /proc file */
    Our_Proc_File = create_proc_entry(PROC_ENTRY_FILENAME, 0644, NULL);
    /* check if the /proc file was created successfuly */
    if (NULL == Our_Proc_File) {
        printk(KERN_ALERT "Error: Could not initialize /proc/%s\n",
            PROC_ENTRY_FILENAME);
        return -ENOMEM;
    }
    
    Our_Proc_File->owner = THIS_MODULE;
    Our_Proc_File->proc_iops = &Inode_Ops_4_Our_Proc_File;
    Our_Proc_File->proc_fops = &File_Ops_4_Our_Proc_File;
    Our_Proc_File->mode = S_IFREG | S_IRUGO | S_IWUSR;
    Our_Proc_File->uid = 0;
    Our_Proc_File->gid = 0;
    Our_Proc_File->size = 80;
    printk(KERN_INFO "/proc/%s created\n", PROC_ENTRY_FILENAME);
    return 0;
}

void cleanup_module()
{
    remove_proc_entry(PROC_ENTRY_FILENAME, &proc_root);
    printk(KERN_INFO "/proc/%s removed\n", PROC_ENTRY_FILENAME);
}
