#include <etcp.h>

int tcp_server(const char *hname, const char *sname)
{
	int sock = 0;
	const int on = 1;
	struct sockaddr_in serv;

	set_address(hname, sname, "tcp", &serv);
	sock = socket(AF_INET, SOCK_STREAM, 0);

	if (sock < 0) {
		error(1, errno, "socket call failed");
    }

	if (0 != setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, (char *)&on, sizeof(on))) {
        error(1, errno, "setsockopt failed");
    }

	if (0 != bind(sock, (struct sockaddr *) &serv, sizeof(serv))) {
        error(1, errno, "bind failed");
    }

	if (0 != listen(sock, NLISTEN)) {
        error(1, errno, "listen failed");
    }

	return sock;
}
