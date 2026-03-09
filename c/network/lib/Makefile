CC = cc
CFLAGS = -c -Wall -Werror -g -O2 -I../include
LIBFCNS = readcrlf.o readline.o readn.o readvrec.o \
		  error.o set_address.o tselect.o smb.o \
		  tcp_client.o tcp_server.o udp_client.o udp_server.o

.c.o :
	$(CC) $(CFLAGS) $< -o $@

all : libetcp.a

libetcp.a : $(LIBFCNS)
	-@rm -f libetcp.a
	ar -q libetcp.a $(LIBFCNS)
	ranlib libetcp.a

clean :
	-@rm -f *.o
	-@rm -f libetcp.a
	-@rm -f libsysv.a
