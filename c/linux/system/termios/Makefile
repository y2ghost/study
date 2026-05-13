ROOT=..
include $(ROOT)/Make.defines
PROGS := csize settty t_getpass t_isatty t_raw t_ttyname winch

all: $(PROGS) ctermid.o getpass.o isatty.o ttyname.o

%: %.c $(LIBCOMMON)
	$(CC) $(CFLAGS) $@.c -o $@ $(LDFLAGS) $(LDLIBS)

clean:
	rm -f $(PROGS) $(TEMPFILES) *.o

include $(ROOT)/Make.lib.inc
