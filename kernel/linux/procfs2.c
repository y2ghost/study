#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <asm/uaccess.h>

#define PROCFS_MAX_SIZE 1024
#define PROCFS_NAME     "buffer1k"

static struct proc_dir_entry *yy_proc = NULL;
static char procfs_buffer[PROCFS_MAX_SIZE] = {'\0'};
static unsigned long procfs_buffer_size = 0;

int procfile_read(char *buffer, char **buffer_location, off_t offset,
    int buffer_length, int *eof, void *data)
{
    int ret = 0;
    
    printk(KERN_INFO "procfile_read (/proc/%s) called\n", PROCFS_NAME);
    
    if (offset > 0) {
        ret  = 0;
    } else {
        memcpy(buffer, procfs_buffer, procfs_buffer_size);
        ret = procfs_buffer_size;
    }
    
    return ret;
}

int procfile_write(struct file *file, const char *buffer,
    unsigned long count, void *data)
{
    /* get buffer size */
    procfs_buffer_size = count;

    if (procfs_buffer_size > PROCFS_MAX_SIZE ) {
        procfs_buffer_size = PROCFS_MAX_SIZE;
    }
    
    /* write data to the buffer */
    if (copy_from_user(procfs_buffer, buffer, procfs_buffer_size)) {
            return -EFAULT;
    }
    
    return procfs_buffer_size;
}

int init_module()
{
    /* create the /proc file */
    yy_proc = create_proc_entry(PROCFS_NAME, 0644, NULL);
        
    if (NULL == yy_proc) {
        remove_proc_entry(PROCFS_NAME, &proc_root);
        printk(KERN_ALERT "Error: Could not initialize /proc/%s\n", PROCFS_NAME);
        return -ENOMEM;
    }

    yy_proc->read_proc  = procfile_read;
    yy_proc->write_proc = procfile_write;
    yy_proc->owner = THIS_MODULE;
    yy_proc->mode = S_IFREG | S_IRUGO;
    yy_proc->uid = 0;
    yy_proc->gid = 0;
    yy_proc->size = 37;
    printk(KERN_INFO "/proc/%s created\n", PROCFS_NAME);    
    return 0;
}

void cleanup_module()
{
    remove_proc_entry(PROCFS_NAME, &proc_root);
    printk(KERN_INFO "/proc/%s removed\n", PROCFS_NAME);
}
