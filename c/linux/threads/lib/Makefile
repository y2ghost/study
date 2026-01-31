ROOT := ..
include $(ROOT)/Make.defines

AR := ar
RANLIB := ranlib
OBJS := $(SOURCES:.c=.o)

$(LIBNAME): $(OBJS)
	$(AR) rv $@ $^
	$(RANLIB) $@

.PHONY: clean
clean:
	rm -f *.o $(LIBNAME)
