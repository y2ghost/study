SOURCES := $(shell echo *.s)
PROGS := $(SOURCES:.s=)
OBJS := $(SOURCES:.s=.o)

# clean default suffixes
.SUFFIXES:

all: $(OBJS) $(PROGS)

%: %.o
	ld -m elf_i386_fbsd $^ -o $@

%.o: %.s
	as -gstabs -32 $^ -o $@

.PHONY: clean
clean:
	@rm -f $(OBJS) $(PROGS)
