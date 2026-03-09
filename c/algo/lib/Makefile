ROOT := ..
include $(ROOT)/Make.defines
OBJS := $(SOURCES:.c=.o)
LIBA := libalgorithm.a

all: $(LIBA)

$(LIBA): $(OBJS)
	ar rv $@ $?
	ranlib $@

clean:
	rm -f *.o $(LIBA)
