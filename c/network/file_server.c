#include <stdio.h>
#include <netdb.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define SERVERPORT  8888
#define MAXBUF      1024

int main(void)
{
    int rc = 0;
    int serv_sock = 0;
    int peer_sock = 0;
    struct sockaddr_in serv_addr;
    struct sockaddr_in peer_addr;

    serv_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (-1 == serv_sock) {
        fprintf(stderr, "could not create socket!\n");
        exit(1);
    }

    memset(&serv_addr, 0x0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htons(INADDR_ANY);
    serv_addr.sin_port = htons(SERVERPORT);

    rc = bind(serv_sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
    if (-1 == rc) {
        fprintf(stderr, "could not bind to socket!\n");
        exit(1);
    }

    rc = listen(serv_sock, 5);
    if (-1 == rc) {
        fprintf(stderr, "could not listen on socket!\n");
        exit(1);
    }

    while (1) {
        int fd = 0;
        socklen_t addr_len = 0;
        int read_counter = 0;
        int write_counter = 0;
        char *bufptr = NULL;
        char buf[MAXBUF] = {'\0'};
        char filename[256] = {'\0'};

        addr_len = sizeof(peer_addr);
        peer_sock = accept(serv_sock, (struct sockaddr*)&peer_addr, &addr_len);

        if (-1 == peer_sock) {
            fprintf(stderr, "could not accept connection!\n");
            exit(1);
        }

        read_counter = read(peer_sock, filename, sizeof(filename));
        if (-1 == read_counter) {
            fprintf(stderr, "could not read filename from socket!\n");
            close(peer_sock);
            continue;
        }

        filename[read_counter + 1] = '\0';
        printf("reading file %s\n", filename);

        fd = open(filename, O_RDONLY);
        if (-1 == fd) {
            fprintf(stderr, "could not open file ror reading!\n");
            close(peer_sock);
            continue;
        }

        while (1) {
            bufptr = buf;
            write_counter = 0;
            read_counter = read(fd, buf, MAXBUF);

            if (read_counter <= 0) {
                break;
            }

            while (write_counter < read_counter) {
                read_counter -= write_counter;
                bufptr += write_counter;
                write_counter = write(peer_sock, bufptr, read_counter);

                if (-1 == write_counter) {
                    fprintf(stderr, "could not write file to client!\n");
                    close(peer_sock);
                    continue;
                }
            }
        }

        close(fd);
        close(peer_sock);
    }

    close(serv_sock);
    return 0;
}
