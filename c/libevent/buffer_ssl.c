#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <openssl/ssl.h>
#include <openssl/err.h>
#include <openssl/rand.h>
#include <event.h>
#include <event2/listener.h>
#include <event2/bufferevent_ssl.h>

static void ssl_readcb(struct bufferevent * bev, void * arg)
{
    struct evbuffer *in = bufferevent_get_input(bev);
    printf("Received %zu bytes\n", evbuffer_get_length(in));
    printf("----- data ----\n");
    printf("%.*s\n", (int)evbuffer_get_length(in), evbuffer_pullup(in, -1));
    bufferevent_write_buffer(bev, in);
}

static void ssl_acceptcb(struct evconnlistener *serv, int sock,
    struct sockaddr *sa, int sa_len, void *arg)
{
    SSL_CTX *server_ctx = (SSL_CTX *)arg;
    SSL *client_ctx = SSL_new(server_ctx);
    struct event_base *evbase = evconnlistener_get_base(serv);
    struct bufferevent *bev = bufferevent_openssl_socket_new(evbase,
        sock, client_ctx,
        BUFFEREVENT_SSL_ACCEPTING,
        BEV_OPT_CLOSE_ON_FREE);
    bufferevent_enable(bev, EV_READ);
    bufferevent_setcb(bev, ssl_readcb, NULL, NULL, NULL);
}

static SSL_CTX *evssl_init(void)
{
    SSL_load_error_strings();
    SSL_library_init();

    if (!RAND_poll()) {
        return NULL;
    }

    SSL_CTX  *server_ctx = SSL_CTX_new(SSLv23_server_method());
    if (! SSL_CTX_use_certificate_chain_file(server_ctx, "cert") ||
        ! SSL_CTX_use_PrivateKey_file(server_ctx, "pkey", SSL_FILETYPE_PEM)) {
        printf("Couldn't read 'pkey' or 'cert' file.  To generate a key\n"
           "and self-signed certificate, run:\n"
           "  openssl genrsa -out pkey 2048\n"
           "  openssl req -new -key pkey -out cert.req\n"
           "  openssl x509 -req -days 365 -in cert.req -signkey pkey -out cert");
        return NULL;
    }

    SSL_CTX_set_options(server_ctx, SSL_OP_NO_SSLv2);
    return server_ctx;
}

int main(int argc, char **argv)
{
    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_port = htons(9999);
    // 127.0.0.1
    sin.sin_addr.s_addr = htonl(0x7f000001);

    SSL_CTX *ctx = evssl_init();
    if (ctx == NULL) {
        return 1;
    }

    struct event_base *evbase = event_base_new();
    struct evconnlistener *listener = evconnlistener_new_bind(evbase,
        ssl_acceptcb, (void *)ctx,
        LEV_OPT_CLOSE_ON_FREE | LEV_OPT_REUSEABLE, 1024,
        (struct sockaddr *)&sin, sizeof(sin));
    event_base_loop(evbase, 0);

    evconnlistener_free(listener);
    SSL_CTX_free(ctx);
    return 0;
}
