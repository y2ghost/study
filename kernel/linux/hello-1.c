#include <linux/module.h>
#include <linux/kernel.h>

int init_module(void)
{
    printk(KERN_INFO "Hello YY kernel world!\n");
    return 0;
}

void cleanup_module(void)
{
    printk(KERN_INFO "Goodbye YY kernel world!\n");
}
