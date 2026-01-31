#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/epoll.h>
#include <errno.h>

#define MAXEVENTS 64

static int _make_socket_non_blocking(int sfd)
{
    int s = 0;
    int flags = 0;

    flags = fcntl(sfd, F_GETFL);
    if (-1 == flags) {
        perror ("fcntl");
        return -1;
    }

    flags |= O_NONBLOCK;
    s = fcntl(sfd, F_SETFL, flags);

    if (-1 == s) {
        perror("fcntl");
        return -1;
    }

    return 0;
}

static int _create_and_bind(char *port)
{
    struct addrinfo hints;
    struct addrinfo *result = NULL;
    struct addrinfo *rp = NULL;
    int s = 0;
    int sfd = 0;
    
    memset(&hints, 0x0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;     /* Return IPv4 and IPv6 choices */
    hints.ai_socktype = SOCK_STREAM; /* We want a TCP socket */
    hints.ai_flags = AI_PASSIVE;     /* All interfaces */
    
    s = getaddrinfo(NULL, port, &hints, &result);
    if (0 != s) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
        return -1;
    }
    
    for (rp=result; NULL!=rp; rp=rp->ai_next) {
        sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
        if (-1 == sfd) {
            continue;
        }
    
        s = bind(sfd, rp->ai_addr, rp->ai_addrlen);
        if (0 == s) {
            /* We managed to bind successfully! */
            break;
        }
    
        close(sfd);
    }
    
    if (NULL == rp) {
        fprintf (stderr, "Could not bind\n");
        return -1;
    }
    
    freeaddrinfo(result);
    return sfd;
}

static void _events_loop(int efd, int sfd, struct epoll_event *events)
{
    int s = 0;

    while (1) {
        int n = 0;
        int i = 0;
    
        n = epoll_wait(efd, events, MAXEVENTS, -1);
        for (i=0; i<n; i++) {
            struct epoll_event *e = events + i;
            int es = e->events;
            int fd = e->data.fd;

            if (es&EPOLLERR || es&EPOLLHUP || (!(es&EPOLLIN))) {
                /* An error has occured on this fd, or the socket is not
                 * ready for reading (why were we notified then?) */
                fprintf(stderr, "epoll error\n");
                close(fd);
                continue;
            } else if (sfd == fd) {
                /* We have a notification on the listening socket, which
                 * means one or more incoming connections. */
                while (1) {
                    struct sockaddr in_addr;
                    socklen_t in_len = 0;
                    int infd = 0;
                    char hbuf[NI_MAXHOST] = {'\0'};
                    char sbuf[NI_MAXSERV] = {'\0'};

                    in_len = sizeof(in_addr);
                    infd = accept(sfd, &in_addr, &in_len);

                    if (-1 == infd) {
                        switch (errno) {
                        case EAGAIN:
                            break;
                        default:
                            perror("accept");
                        }

                        break;
                    }
    
                    s = getnameinfo(&in_addr, in_len,
                        hbuf, sizeof(hbuf), sbuf, sizeof(sbuf),
                        NI_NUMERICHOST | NI_NUMERICSERV);
                    if (0 == s) {
                        printf("Accepted connection on descriptor %d "
                            "(host=%s, port=%s)\n", infd, hbuf, sbuf);
                    }
    
                    /* Make the incoming socket non-blocking and add it to the
                     * list of fds to monitor. */
                    s = _make_socket_non_blocking (infd);
                    if (-1 == s) {
                        abort();
                    }
    
                    e->data.fd = infd;
                    e->events = EPOLLIN | EPOLLET;

                    s = epoll_ctl(efd, EPOLL_CTL_ADD, infd, e);
                    if (-1 == s) {
                        perror("epoll_ctl");
                        abort();
                    }
                }
                
                continue;
            } else {
                /* We have data on the fd waiting to be read. Read and
                 * display it. We must read whatever data is available
                 * completely, as we are running in edge-triggered mode
                 * and won't get a notification again for the same data. */
                int done = 0;
                while (1) {
                    ssize_t count = 0;
                    char buf[512] = {'\0'};
    
                    count = read(fd, buf, sizeof(buf));
                    if (-1 == count) {
                        /* If errno == EAGAIN, that means we have read all
                         * data. So go back to the main loop. */
                        if (EAGAIN != errno) {
                            perror("read");
                            done = 1;
                        }

                        break;
                    } else if (0 == count) {
                        /* End of file. The remote has
                         * closed the connection. */
                        done = 1;
                        break;
                    }
    
                    /* Write the buffer to standard output */
                    s = write(STDIN_FILENO, buf, count);
                    if (-1 == s) {
                        perror("write");
                        abort();
                    }
                }
    
                if (0 != done) {
                    printf("Closed connection on descriptor %d\n", fd);
                    /* Closing the descriptor will make epoll remove it
                       from the set of descriptors which are monitored. */
                    close(fd);
                }
            }
        }
    }
}

int main(int argc, char *argv[])
{
    if (2 != argc) {
        fprintf(stderr, "Usage: %s [port]\n", argv[0]);
        exit (EXIT_FAILURE);
    }
    
    int sfd = _create_and_bind(argv[1]);
    if (-1 == sfd) {
        abort();
    }
    
    int s = _make_socket_non_blocking(sfd);
    if (-1 == s) {
        abort();
    }
    
    s = listen(sfd, SOMAXCONN);
    if (-1 == s) {
        perror ("listen");
        abort();
    }
    
    int efd = epoll_create1(0);
    if (-1 == efd) {
        perror("epoll_create");
        abort();
    }
    
    struct epoll_event event;
    memset(&event, 0x0, sizeof(event));
    event.data.fd = sfd;
    event.events = EPOLLIN | EPOLLET;

    s = epoll_ctl(efd, EPOLL_CTL_ADD, sfd, &event);
    if (-1 == s) {
        perror("epoll_ctl");
        abort();
    }
    
    /* Buffer where events are returned */
    struct epoll_event *events = NULL;
    events = calloc(MAXEVENTS, sizeof(*events));

    if (NULL == events) {
        perror("calloc");
        abort();
    }

    /* The event loop */
    _events_loop(efd, sfd, events);
    free(events);
    close(sfd);
    return EXIT_SUCCESS;
}
