#ifndef __ETCP_H__
#define __ETCP_H__

#include <errno.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define TRUE        1
#define FALSE       0
#define NLISTEN     5
#define NSMB        5
#define SMBUFSZ     256

typedef void (*tofunc_t)(void *arg);

void error( int status, int err, char *fmt, ... );
int readn(int sock, char *bp, size_t len);
int readvrec(int sock, char *bp, size_t len);
int readcrlf(int sock, char *buf, size_t len);
int readline(int sock, char *bufptr, size_t len);
int tcp_server(const char *hname, const char *sname);
int tcp_client(const char *hname, const char *sname);
int udp_server(const char *hname, const char *sname);
int udp_client(const char *hname, const char *sname, struct sockaddr_in *sap);
unsigned int timeout(tofunc_t func, void *arg, int ms);
void untimeout(unsigned int id);
int tselect(int maxp1, fd_set *re, fd_set *we, fd_set *ee);
void init_smb(int init_freelist);
void *smballoc(void);
void smbfree(void *smb);
void smbsend(int sock, void *);
void *smbrecv(int sock);
void set_address(const char *hname, const char *sname, const char *protocol,
    struct sockaddr_in *sap);

#endif  /* __ETCP_H__ */
