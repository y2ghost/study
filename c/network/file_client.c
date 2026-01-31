#include <stdio.h>
#include <netdb.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#define SERVERPORT  8888
#define MAXBUF      1024

int main(int argc, char *argv[])
{
    int rc = 0;
    int fd  = 0;
    int len = 0;
    int serv_sock = 0;
    int read_bytes = 0;
    struct sockaddr_in serv_addr;
    char *filename = NULL;
    char buf[MAXBUF] = {'\0'};

    if (argc < 3) {
        fprintf(stderr, "usage: %s <ip address> <filename> [dest filename]\n",
            argv[0]);
        exit(1);
    }

    serv_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (-1 == serv_sock) {
        fprintf(stderr, "could not create socket!\n");
        exit(1);
    }

    memset(&serv_addr, 0x0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    inet_pton(AF_INET, argv[1], &serv_addr.sin_addr);
    serv_addr.sin_port = htons(SERVERPORT);

    rc = connect(serv_sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
    if (-1 == rc) {
        fprintf(stderr, "could not connect to server!\n");
        exit(1);
    }

    filename = argv[2];
    len = strlen(filename) + 1;

    rc = write(serv_sock, filename, len);
    if (-1 == rc) {
        fprintf(stderr, "could not send filename to server!\n");
        exit(1);
    }

    shutdown(serv_sock, SHUT_WR);
    fd = open(argv[3], O_WRONLY|O_CREAT|O_APPEND);

    if (-1 == fd) {
        fd = STDOUT_FILENO;
    }

    while (1) {
        read_bytes = read(serv_sock, buf, MAXBUF);
        if (read_bytes <= 0) {
            break;
        }

        write(fd, buf, read_bytes);
    }

    if (-1 == read_bytes) {
        fprintf(stderr, "could not read file from socket!\n");
        exit(1);
    }

    close(serv_sock);
    return 0;
}
