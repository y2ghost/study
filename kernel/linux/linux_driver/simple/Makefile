ifeq ($(DEBUG),y)
  DEBFLAGS = -O -g
else
  DEBFLAGS = -O2
endif

CFLAGS += $(DEBFLAGS) -I$(LDDINCDIR)

ifneq ($(KERNELRELEASE),)
# call from kernel build system
obj-m	:= simple.o
else
KERNELDIR ?= /lib/modules/$(shell uname -r)/build
PWD       := $(shell pwd)

default:
	$(MAKE) -C $(KERNELDIR) M=$(PWD) LDDINCDIR=$(PWD)/../include modules
endif

clean:
	rm -rf *.o *~ core .depend .*.cmd *.ko *.mod.c .tmp_versions \
	Module.markers Module.symvers

depend .depend dep:
	$(CC) $(CFLAGS) -M *.c > .depend

ifeq (.depend,$(wildcard .depend))
include .depend
endif
