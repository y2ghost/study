ROOT=..
include $(ROOT)/Make.defines

PROGS := $(SOURCES:.c=)

all: $(PROGS)

%:	%.c $(LIBAPUE)
	$(CC) $(CFLAGS) $@.c -o $@ $(LDFLAGS) $(LDLIBS)

clean:
	rm -f $(PROGS)

include $(ROOT)/Make.libapue.inc
