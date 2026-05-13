ROOT=.
include Make.defines

PROGS := $(SOURCES:.c=)

all: $(PROGS)

%: %.c
	cc -o $@ $(CFLAGS) $^ $(LDFLAGS)

events: events.c
	cc -o $@ $(CFLAGS) $^ $(LDFLAGS) -lX11

simple-%: simple-%.c
	cc -o $@ $(CFLAGS) $^ $(LDFLAGS) -lX11

line-count: line-count.c
	cc -o $@ $(CFLAGS) $^ $(LDFLAGS) -lpthread

clean:
	@rm -f $(PROGS); echo clean all programs ok
