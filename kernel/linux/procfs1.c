#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>

#define procfs_name "yyproc"

struct proc_dir_entry *yy_proc = NULL;

int procfile_read(char *buffer, char **buffer_location, off_t offset,
    int buffer_length, int *eof, void *data)
{
    int ret = 0;
    
    printk(KERN_INFO "procfile_read (/proc/%s) called\n", procfs_name);
    
    if (offset > 0) {
        ret  = 0;
    } else {
        ret = sprintf(buffer, "HelloWorld!\n");
    }
    
    return ret;
}

int init_module()
{
    yy_proc = create_proc_entry(procfs_name, 0644, NULL);

    if (NULL == yy_proc) {
        remove_proc_entry(procfs_name, &proc_root);
        printk(KERN_ALERT "Error: Could not initialize /proc/%s\n",
            procfs_name);
        return -ENOMEM;
    }

    yy_proc->read_proc = procfile_read;
    yy_proc->owner = THIS_MODULE;
    yy_proc->mode = S_IFREG | S_IRUGO;
    yy_proc->uid = 0;
    yy_proc->gid = 0;
    yy_proc->size = 37;
    printk(KERN_INFO "/proc/%s created\n", procfs_name);    
    return 0;
}

void cleanup_module()
{
    remove_proc_entry(procfs_name, &proc_root);
    printk(KERN_INFO "/proc/%s removed\n", procfs_name);
}
