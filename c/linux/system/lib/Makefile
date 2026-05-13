# make libcommon.a
ROOT=..
include $(ROOT)/Make.defines

LIBMISC	:= libcommon.a
OBJS := $(SOURCES:.c=.o)

all: $(LIBMISC) sleep.o

$(LIBMISC):	$(OBJS)
	$(AR) rv $(LIBMISC) $?
	$(RANLIB) $(LIBMISC)

clean:
	rm -f *.o a.out core temp.* $(LIBMISC)
