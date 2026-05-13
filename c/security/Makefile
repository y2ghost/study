ROOT := .
include $(ROOT)/Make.defines

PROGS := $(SOURCES:.c=)

.PHONY: all clean

all: $(PROGS)

clean:
	rm -f $(TEMPFILES)
	rm -f $(PROGS)
