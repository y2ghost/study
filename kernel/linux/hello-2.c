#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>

static int hello2_init(void)
{
    printk(KERN_INFO "Hello2 YY kernel world!\n");
    return 0;
}

static void hello2_exit(void)
{
    printk(KERN_INFO "Goodbye2 YY kernel world!\n");
}

module_init(hello2_init);
module_exit(hello2_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Yong.Yang");
MODULE_DESCRIPTION("a sample test");
MODULE_SUPPORTED_DEVICE("yydevice");
