ROOT=..
include $(ROOT)/Make.defines

PROGS := strftime

all: $(PROGS) getpwnam.o

%:	%.c $(LIBAPUE)
	$(CC) $(CFLAGS) $@.c -o $@ $(LDFLAGS) $(LDLIBS)

clean:
	rm -f $(PROGS) $(TEMPFILES) *.o

include $(ROOT)/Make.libapue.inc
