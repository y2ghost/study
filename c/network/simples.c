#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>

int main(int ac, char **av)
{
    int rc = -1;
    int sock = -1;
    int clnt_sock = -1;
    socklen_t len = -1;
    char buf[1] = {'\0'};
    struct sockaddr_in serv;
    struct sockaddr_in clnt;

    serv.sin_family = AF_INET;
    serv.sin_port = htons(7500);
    serv.sin_addr.s_addr = htonl(INADDR_ANY);
    sock = socket(AF_INET, SOCK_STREAM, 0);

    if (sock < 0) {
        perror("socket call failed");
        return 1;
    }

    rc = bind(sock, (struct sockaddr*)&serv, sizeof(serv));
    if (rc < 0) {
        perror("bind call failed");
        return 1;
    }

    rc = listen(sock, 5);
    if (0 !=  rc) {
        perror("listen call failed");
        return 1;
    }

    len = sizeof(clnt);
    clnt_sock = accept(sock, (struct sockaddr*)&clnt, &len);

    if (clnt_sock < 0) {
        perror("accept call failed");
        return 1;
    }

    rc = recv(clnt_sock, buf, sizeof(buf), 0);
    if (rc <= 0) {
        perror("recv call failed");
        return 1;
    }

    printf("%c\n", *buf);
    rc = send(clnt_sock, "2", 1, 0);

    if (rc <= 0) {
        perror("send call failed");
        return 1;
    }

    return 0;
}
