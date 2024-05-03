SOURCES := $(shell echo *.s)
PROGS := $(SOURCES:.s=)

# clean default suffixes
.SUFFIXES:

all: $(PROGS)

%: %.s
	as -gstabs -32 $^ -o $@.o
	ld -m elf_i386 -dynamic-linker /lib/ld-linux.so.2 \
	-o $@ -L/usr/lib -lc $@.o

.PHONY: clean
clean:
	@rm -f $(PROGS) *.o
