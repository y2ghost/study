#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <poll.h>
#include <arpa/inet.h>

#define BUFSIZE 1024

void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(int argc, char *argv[])
{
	if (argc != 3) {
	    fprintf(stderr,"usage: telnot hostname port\n");
	    exit(1);
	}

	char *hostname = argv[1];
	char *port = argv[2];
	struct addrinfo hints;
	struct addrinfo *servinfo = NULL;
	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	int rv = getaddrinfo(hostname, port, &hints, &servinfo);

    if (0 != rv) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
		return 1;
	}

    struct addrinfo *p = NULL;
    int sockfd = 0;

	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			//perror("telnot: socket");
			continue;
		}

		if (connect(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			//perror("telnot: connect");
			close(sockfd);
			continue;
		}

		break;
	}

	if (p == NULL) {
		fprintf(stderr, "client: failed to connect\n");
		return 2;
	}

    char s[INET6_ADDRSTRLEN] = {0};
	inet_ntop(p->ai_family, get_in_addr((struct sockaddr *)p->ai_addr),
			s, sizeof s);
	printf("Connected to %s port %s\n", s, port);
	printf("Hit ^C to exit\n");
	freeaddrinfo(servinfo);

	struct pollfd fds[2];
	fds[0].fd = 0;
	fds[0].events = POLLIN;
	fds[1].fd = sockfd;
	fds[1].events = POLLIN;

	for(;;) {
		if (poll(fds, 2, -1) == -1) {
			perror("poll");
			exit(1);
		}

		for (int i = 0; i < 2; i++) {
			if (fds[i].revents & POLLIN) {
				int readbytes = 0;
                int writebytes = 0;
				char buf[BUFSIZE] = {0};
				int outfd = fds[i].fd == 0? sockfd: 1;

				if ((readbytes = read(fds[i].fd, buf, BUFSIZE)) == -1) {
					perror("read");
					exit(2);
				}

				char *p = buf;
				int remainingbytes = readbytes;

				while (remainingbytes > 0) {
					if ((writebytes = write(outfd, p, remainingbytes)) == -1) {
						perror("write");
						exit(2);
					}

					p += writebytes;
					remainingbytes -= writebytes;
				}
			}
		}
	}

	return 0;
}

