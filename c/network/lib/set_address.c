#include <etcp.h>
#include <netdb.h>
#include <string.h>
#include <arpa/inet.h>

void set_address(const char *hname, const char *sname, const char *protocol,
    struct sockaddr_in *sap)
{
    struct sockaddr_in sap_;
	struct servent *sp = NULL;
	struct hostent *hp = NULL;
	char *endptr = NULL;
	short port = 0;

    memset(&sap_, 0x0, sizeof(sap_));
	sap_.sin_family = AF_INET;

	if (hname != NULL) {
		if (!inet_aton(hname, &sap_.sin_addr)) {
			hp = gethostbyname(hname);
			if (hp == NULL) {
                error(1, 0, "unknown host: %s\n", hname);
            }

			sap_.sin_addr = *(struct in_addr *)hp->h_addr;
		}
	} else {
        sap_.sin_addr.s_addr = htonl(INADDR_ANY);
    }

	port = strtol(sname, &endptr, 0);
	if (*endptr == '\0') {
        sap_.sin_port = htons(port);
    } else {
		sp = getservbyname(sname, protocol);
		if (sp == NULL) {
			error(1, 0, "unknown service: %s\n", sname);
        }

		sap_.sin_port = sp->s_port;
	}

    memcpy(sap, &sap_, sizeof(*sap));
}
