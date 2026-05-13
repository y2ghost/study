CC := cc
CFLAGS := -Wall -Werror -g -O2 -I/opt/progs/include
LDFLAGS := -L/opt/progs/lib -lnanomsg -lrt
LDFLAGS := /opt/progs/lib/libnanomsg.a -lpthread
SOURCES := $(shell echo *.c)
PROGS := $(SOURCES:.c=)

all: $(PROGS)

%: %.c
	$(CC) -o $@ $(CFLAGS) $^ $(LDFLAGS)

.PHONY: clean
clean:
	rm -f *.o $(PROGS)
