ROOT := ..
include $(ROOT)/Make.defines
PROGS := $(SOURCES:.c=)

all: $(PROGS)

%: %.c $(LIB)
	$(CC) $(CFLAGS) $@.c -o $@ $(LDFLAGS) $(LDLIBS)

clean:
	@rm -f $(PROGS)
	@rm -rf *.dSYM

$(LIB):
	(cd $(ROOT)/lib && $(MAKE))
