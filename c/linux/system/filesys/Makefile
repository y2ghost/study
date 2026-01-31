ROOT=..
include $(ROOT)/Make.defines

PROGS := $(SOURCES:.c=)

all: $(PROGS)

%:	%.c $(LIBCOMMON)
	$(CC) $(CFLAGS) $@.c -o $@ $(LDFLAGS) $(LDLIBS) -lacl

clean:
	rm -f $(PROGS) $(TEMPFILES)

include $(ROOT)/Make.lib.inc
