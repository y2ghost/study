#include "scullp.h"     /* local definitions */
#include <linux/autoconf.h>
#include <linux/module.h>
#include <linux/mm.h>       /* everything */
#include <asm/pgtable.h>

/*
 * open and close: just keep track of how many times the device is
 * mapped, to avoid releasing it.
 */
void scullp_vma_open(struct vm_area_struct *vma)
{
    struct scullp_dev *dev = vma->vm_private_data;

    dev->vmas++;
}

void scullp_vma_close(struct vm_area_struct *vma)
{
    struct scullp_dev *dev = vma->vm_private_data;

    dev->vmas--;
}

/*
 * The nopage method: the core of the file. It retrieves the
 * page required from the scullp device and returns it to the
 * user. The count for the page must be incremented, because
 * it is automatically decremented at page unmap.
 *
 * For this reason, "order" must be zero. Otherwise, only the first
 * page has its count incremented, and the allocating module must
 * release it as a whole block. Therefore, it isn't possible to map
 * pages from a multipage block: when they are unmapped, their count
 * is individually decreased, and would drop to 0.
 */
struct page *scullp_vma_nopage(struct vm_area_struct *vma,
    unsigned long address, int *type)
{
    unsigned long offset = 0;
    struct scullp_dev *ptr = NULL;
    struct scullp_dev *dev = vma->vm_private_data;
    struct page *page = NOPAGE_SIGBUS;
    void *pageptr = NULL; /* default to "missing" */

    down(&dev->sem);
    offset = (address - vma->vm_start) + (vma->vm_pgoff << PAGE_SHIFT);

    if (offset >= dev->size) {
        goto out; /* out of range */
    }

    /*
     * Now retrieve the scullp device from the list,then the page.
     * If the device has holes, the process receives a SIGBUS when
     * accessing the hole.
     */
    offset >>= PAGE_SHIFT; /* offset is a number of pages */
    for (ptr=dev; NULL!=ptr && offset>=dev->qset;) {
        ptr = ptr->next;
        offset -= dev->qset;
    }

    if (NULL!=ptr && NULL!=ptr->data) {
        pageptr = ptr->data[offset];
    }

    if (NULL == pageptr) {
        goto out; /* hole or end-of-file */
    }

    page = virt_to_page(pageptr);
    /* got it, now increment the count */
    get_page(page);

    if (NULL != type) {
        *type = VM_FAULT_MINOR;
    }

out:
    up(&dev->sem);
    return page;
}

struct vm_operations_struct scullp_vm_ops = {
    .open = scullp_vma_open,
    .close = scullp_vma_close,
    .nopage = scullp_vma_nopage,
};

int scullp_mmap(struct file *filp, struct vm_area_struct *vma)
{
    struct inode *inode = filp->f_dentry->d_inode;

    /* refuse to map if order is not 0 */
    if (scullp_devices[iminor(inode)].order) {
        return -ENODEV;
    }

    /* don't do anything here: "nopage" will set up page table entries */
    vma->vm_ops = &scullp_vm_ops;
    vma->vm_flags |= VM_RESERVED;
    vma->vm_private_data = filp->private_data;
    scullp_vma_open(vma);
    return 0;
}
