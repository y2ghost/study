#include <time.h>
#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <unistd.h>
#include <nanomsg/nn.h>
#include <nanomsg/pubsub.h>

static const char SERVER[] = "server";
static const char CLIENT[] = "client";

static char *_date(void)
{
    time_t raw = time(&raw);
    struct tm *info = localtime(&raw);
    char *text = asctime (info);
    text[strlen(text)-1] = '\0';    /* remove '\n' */
    return text;
}

static int server(const char *url)
{
    if (NULL == url) {
        return -1;
    }

    int sock = nn_socket(AF_SP, NN_PUB);
    assert(sock >= 0);
    assert(nn_bind(sock, url) >= 0);

    while (1) {
        char *d = _date();
        int sz_d = strlen(d) + 1;
        printf ("SERVER: PUBLISHING DATE %s\n", d);
        int bytes = nn_send(sock, d, sz_d, 0);
        assert(bytes == sz_d);
        sleep(1);
    }

    return nn_shutdown(sock, 0);
}

static int client(const char *url, const char *name)
{
    if (NULL == url) {
        return -1;
    }

    int sock = nn_socket(AF_SP, NN_SUB);
    assert(sock >= 0);
    assert(nn_setsockopt(sock, NN_SUB, NN_SUB_SUBSCRIBE, "", 0) >= 0);
    assert(nn_connect(sock, url) >= 0);

    while (1) {
        char *buf = NULL;
        int bytes = nn_recv(sock, &buf, NN_MSG, 0);
        assert(bytes >= 0);
        printf("CLIENT (%s): RECEIVED %s\n", name, buf);
        nn_freemsg(buf);
    }

    return nn_shutdown(sock, 0);
}

static void _usage(void)
{
    fprintf(stderr, "Usage: pubsub %s|%s <URL> <ARG> ...\n",
        SERVER, CLIENT);
}

int main(const int argc, const char **argv)
{
    if (argc < 3) {
        _usage();
        return 1;
    }

    const char *node_name = argv[1];
    const char *url = argv[2];

    if (0 == strcmp(SERVER, node_name)) {
        return server(url);
    }

    if (0==strcmp(CLIENT, node_name) && argc>3) {
        const char *name = argv[3];
        return client(url, name);
    }

    _usage();
    return 1;
}
