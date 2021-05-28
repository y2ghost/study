# make libapue.a
ROOT=..
include $(ROOT)/Make.defines

LIBMISC	:= libapue.a
OBJS := $(SOURCES:.c=.o)

all: $(LIBMISC) sleep.o

$(LIBMISC):	$(OBJS)
	$(AR) rv $(LIBMISC) $?
	$(RANLIB) $(LIBMISC)

clean:
	rm -f *.o a.out core temp.* $(LIBMISC)
