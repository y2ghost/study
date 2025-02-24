#include <assert.h>
#include <string.h>
#include <time.h>
#include <stdio.h>
#include <nanomsg/nn.h>
#include <nanomsg/reqrep.h>

static const char NODE0[] = "node0";
static const char NODE1[] = "node1";
static const char DATE[] = "DATE";

static char *_date(void)
{
    time_t raw = time(&raw);
    struct tm *info = localtime(&raw);
    char *text = asctime(info);
    text[strlen(text)-1] = '\0';
    return text;
}

static int node0(const char *url)
{
    if (NULL == url) {
        fprintf(stderr, "miss url\n");
        return -1;
    }

    int sock = nn_socket(AF_SP, NN_REP);
    assert(sock >= 0);
    assert(nn_bind(sock, url) >= 0);

    while (1) {
        char *buf = NULL;
        int bytes = nn_recv(sock, &buf, NN_MSG, 0);

        assert(bytes >= 0);
        if (0 == strcmp(DATE, buf)) {
            printf("NODE0: RECEIVED DATE REQUEST\n");
            char *d = _date();
            int sz_d = strlen(d) + 1;
            printf("NODE0: SENDING DATE %s\n", d);
            bytes = nn_send(sock, d, sz_d, 0);
            assert(bytes == sz_d);
        }

        nn_freemsg(buf);
    }

    return nn_shutdown(sock, 0);
}

static int node1(const char *url)
{
    if (NULL == url) {
        fprintf(stderr, "miss url info\n");
        return -1;
    }

    int sock = nn_socket(AF_SP, NN_REQ);
    assert(sock >= 0);
    assert(nn_connect(sock, url) >= 0);
    printf("NODE1: SENDING DATE REQUEST %s\n", DATE);
    int sz_date = sizeof(DATE);
    int bytes = nn_send(sock, DATE, sz_date, 0);
    assert(bytes == sz_date);
    char *buf = NULL;
    bytes = nn_recv(sock, &buf, NN_MSG, 0);
    assert(bytes >= 0);
    printf("NODE1: RECEIVED DATE %s\n", buf);
    nn_freemsg(buf);
    return nn_shutdown(sock, 0);
}

static void _usage(void)
{
    fprintf(stderr, "Usage: reqrep %s|%s <URL> <ARG> ...\n",
        NODE0, NODE1);
}

int main(const int argc, const char **argv)
{
    if (argc < 3) {
        _usage();
        return 1;
    }

    const char *node_name = argv[1];
    const char *url = argv[2];

    if (0 == strcmp(NODE0, node_name)) {
        return node0(url);
    }

    if (0 == strcmp(NODE1, node_name)) {
        return node1(url);
    }

    _usage();
    return 1;
}
