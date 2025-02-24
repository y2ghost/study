#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <assert.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>
#include <stdlib.h>
#include <sys/epoll.h>
#include <pthread.h>
#include <libgen.h>

#define MAX_EVENT_NUMBER 1024
#define BUFFER_SIZE 10

int setnonblocking(int fd)
{
    int old_option = fcntl(fd, F_GETFL);
    int new_option = old_option | O_NONBLOCK;
    fcntl(fd, F_SETFL, new_option);
    return old_option;
}

void addfd(int epollfd, int fd, int enable_et)
{
    struct epoll_event event;
    event.data.fd = fd;
    event.events = EPOLLIN;

    if(0 != enable_et) {
        event.events |= EPOLLET;
    }

    epoll_ctl(epollfd, EPOLL_CTL_ADD, fd, &event);
    setnonblocking(fd);
}

void lt(struct epoll_event* events, int number, int epollfd, int listenfd)
{
    int i = 0;
    char buf[BUFFER_SIZE] = {'\0'};

    for (i=0; i<number; i++) {
        int sockfd = events[i].data.fd;
        if (sockfd == listenfd) {
            struct sockaddr_in client_address;
            socklen_t client_addrlength = sizeof(client_address);
            int connfd = accept(listenfd, (struct sockaddr*)&client_address,
                &client_addrlength);
            addfd(epollfd, connfd, 0);
        } else if (events[i].events & EPOLLIN) {
            printf("event trigger once\n");
            memset(buf, '\0', BUFFER_SIZE);

            int ret = recv(sockfd, buf, BUFFER_SIZE-1, 0);
            if (ret <= 0) {
                close(sockfd);
                continue;
            }

            printf("get %d bytes of content: %s\n", ret, buf);
        } else {
            printf("something else happened \n");
        }
    }
}

void et(struct epoll_event* events, int number, int epollfd, int listenfd)
{
    int i = 0;
    char buf[BUFFER_SIZE] = {'\0'};

    for (i=0; i<number; i++) {
        int sockfd = events[i].data.fd;
        if (sockfd == listenfd) {
            struct sockaddr_in client_address;
            socklen_t client_addrlength = sizeof(client_address);
            int connfd = accept(listenfd, (struct sockaddr*)&client_address,
                &client_addrlength);
            addfd(epollfd, connfd, 1);
        } else if (events[i].events & EPOLLIN) {
            printf("event trigger once\n");
            while(1) {
                memset(buf, '\0', BUFFER_SIZE);
                int ret = recv(sockfd, buf, BUFFER_SIZE-1, 0);
                
                if (ret < 0) {
                    if (EAGAIN==errno || EWOULDBLOCK==errno) {
                        printf("read later\n");
                        break;
                    }

                    close(sockfd);
                    break;
                } else if(0 == ret) {
                    close(sockfd);
                } else {
                    printf("get %d bytes of content: %s\n", ret, buf);
                }
            }
        } else {
            printf("something else happened \n");
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc <= 2) {
        const char *prog_name = basename(argv[0]);
        printf("usage: %s ip_address port_number\n", prog_name);
        return 1;
    }

    const char* ip = argv[1];
    int port = atoi(argv[2]);

    int ret = 0;
    struct sockaddr_in address;
    memset(&address, 0x0, sizeof(address));
    address.sin_family = AF_INET;
    inet_pton(AF_INET, ip, &address.sin_addr);
    address.sin_port = htons(port);

    int listenfd = socket(PF_INET, SOCK_STREAM, 0);
    assert(listenfd >= 0);

    ret = bind(listenfd, (struct sockaddr*)&address, sizeof(address));
    assert(-1 != ret);

    ret = listen(listenfd, 5);
    assert(-1 != ret);

    struct epoll_event events[MAX_EVENT_NUMBER];
    int epollfd = epoll_create(5);
    assert(-1 != epollfd);

    addfd(epollfd, listenfd, 1);
    while(1) {
        int ret = epoll_wait(epollfd, events, MAX_EVENT_NUMBER, -1);
        if (ret < 0) {
            printf("epoll failure\n");
            break;
        }
    
        lt(events, ret, epollfd, listenfd);
    }

    close(listenfd);
    return 0;
}
