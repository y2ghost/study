ifeq ($(KERNELRELEASE),)
build-dir := /lib/modules/$(shell uname -r)/build

all:
	make -C $(build-dir) M=$(PWD) modules

clean:
	make -C $(build-dir) M=$(PWD) clean
	rm -rf Module.markers Module.symvers

else
obj-m += hello-1.o
obj-m += hello-2.o
obj-m += hello-3.o
obj-m += chardev.o
obj-m += startstop.o
obj-m += procfs1.o
obj-m += procfs2.o
obj-m += procfs3.o
obj-m += procfs4.o
startstop-objs := start.o  stop.o

endif
