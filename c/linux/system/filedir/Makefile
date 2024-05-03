ROOT=..
include $(ROOT)/Make.defines

PROGS := $(SOURCES:.c=)

all: $(PROGS)

%:	%.c $(LIBCOMMON)
	$(CC) $(CFLAGS) $@.c -o $@ $(LDFLAGS) $(LDLIBS)

clean:
	rm -f $(PROGS)

include $(ROOT)/Make.lib.inc
