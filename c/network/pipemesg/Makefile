CC := cc
CFLAGS := -g -O2 -D_REENTRANT -Wall -Werror
PROGS := mainpipe mainfifo
OBJS := mesg.o util.o

all: ${PROGS}

mainpipe: mainpipe.o ${OBJS}
	${CC} ${CFLAGS} -o $@ $^

mainfifo: mainfifo.o ${OBJS}
	${CC} ${CFLAGS} -o $@ $^

clean:
	rm -f ${PROGS} *.o
