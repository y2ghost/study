#include <assert.h>
#include <string.h>
#include <stdio.h>
#include <nanomsg/nn.h>
#include <nanomsg/pipeline.h>

static const char NODE0[] = "node0";
static const char NODE1[] = "node1";

static int node0(const char *url)
{
    if (NULL == url) {
        fprintf(stderr, "miss url\n");
        return -1;
    }

    int sock = nn_socket(AF_SP, NN_PULL);
    assert(sock >= 0);
    assert(nn_bind(sock, url) >= 0);

    while (1) {
        char *buf = NULL;
        int bytes = nn_recv(sock, &buf, NN_MSG, 0);

        assert(bytes >= 0);
        printf("NODE0: RECEIVED \"%s\"\n", buf);
        nn_freemsg(buf);
    }
}

static int node1(const char *url, const char *msg)
{
    if (NULL==url || NULL==msg) {
        fprintf(stderr, "miss url or msg info\n");
        return -1;
    }

    int sz_msg = strlen(msg) + 1;
    int sock = nn_socket(AF_SP, NN_PUSH);

    assert(sock >= 0);
    assert(nn_connect(sock, url) >= 0);
    printf("NODE1: SENDING \"%s\"\n", msg);

    int bytes = nn_send(sock, msg, sz_msg, 0);
    assert(bytes == sz_msg);
    return nn_shutdown(sock, 0);
}

static void _usage(void)
{
    fprintf(stderr, "Usage: pipeline %s|%s <URL> <ARG> ...\n",
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

    if (0 == strcmp(NODE1, node_name) && argc>3) {
        const char *msg = argv[3];
        return node1(url, msg);
    } else {
        _usage();
    }

    return 1;
}
