ROOT=.
include Make.defines

PROGS := $(SOURCES:.c=)

all: $(ROOT)/lib/$(LIBNAME) $(PROGS)

$(ROOT)/lib/$(LIBNAME): lib
	make -C $<

%: %.c $(ROOT)/lib/$(LIBNAME)
	cc -o $@ $(CFLAGS) $^ $(LDFLAGS)

clean:
	make -C lib $@
	@rm -f $(PROGS); echo clean all programs ok
