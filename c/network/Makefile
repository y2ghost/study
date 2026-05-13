MAKE := make
CC := cc
CFLAGS := -g -O2 -Wall -Werror -Iinclude
LDFLAGS := lib/libetcp.a
SOURCES := $(shell echo *.c)
PROGS := $(SOURCES:.c=)

all : $(PROGS)

%: %.c lib/libetcp.a
	$(CC) $(CFLAGS) $< -o $@ $(LDFLAGS)

lib/libetcp.a :
	(cd lib; $(MAKE))

clean :
	-rm -f *.o
	-rm -f $(PROGS)
	(cd lib; $(MAKE) clean)
