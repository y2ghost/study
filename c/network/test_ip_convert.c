#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main(void)
{
    // 字符串转结构体
    struct sockaddr_in sa4;
    struct sockaddr_in6 sa6;
    struct sockaddr_storage ss;

    printf("sizeof(sockaddr_in)=%ld\n", sizeof(sa4));
    printf("sizeof(sockaddr_in6)=%ld\n", sizeof(sa6));
    printf("sizeof(sockaddr_storage)=%ld\n", sizeof(ss));
    inet_pton(AF_INET, "192.168.1.254", &(sa4.sin_addr));
    inet_pton(AF_INET6, "2001:db8:63b3:1::3490", &(sa6.sin6_addr));

    // 结构体转字符串
    char ip4[INET_ADDRSTRLEN] = {0};
    inet_ntop(AF_INET, &(sa4.sin_addr), ip4, INET_ADDRSTRLEN);
    printf("ipv4地址: %s\n", ip4);

    char ip6[INET6_ADDRSTRLEN] = {0};
    inet_ntop(AF_INET6, &(sa6.sin6_addr), ip6, INET6_ADDRSTRLEN);
    printf("ipv4地址: %s\n", ip6);

    // 结构体手工转IPV4地址
    uint8_t *bytes = (uint8_t*)&(sa4.sin_addr);
    printf("ipv4地址(手工): %d.%d.%d.%d\n", bytes[0], bytes[1], bytes[2], bytes[3]);
    return 0;
}

