构建测试
- make

client-server模式
- ./server
- ./client localhost
- ./listener
- ./talker localhost good

字节转换函数
- htons() host to network short
- htonl() host to network long
- ntohs() network to host short
- ntohl() network to host long

addrinfo结构体概述
- ```c
struct addrinfo {
    int ai_flags;   // AI_PASSIVE, AI_CANONNAME等
    int ai_family;  // AF_INET, AF_INET6, AF_UNSPEC
    int ai_socktype;    // SOCK_STREAM, SOCK_DGRAM
    int ai_protocol;    // 0(任意协议)
    socklen_t ai_addrlen;       // ai_addr长度
    struct sockaddr *ai_addr;   // struct sockaddr_{in|in6}结构体
    char *ai_canonname;         // 完整规范主机名
    struct addrinfo *ai_next;   // 链表下一个节点
};
- ```

sockaddr_in结构体概述
- ```c
struct sockaddr_in {
    short int sin_family;           // AF_INET
    unsigned short int sin_port;    // 端口(网络字节序)
    struct in_addr sin_addr;        // 地址
    unsigned char sin_zero[8];      // 填充
};
struct in_addr {
    uint32_t s_addr;    // IPV4地址(网络字节序)
};
- ```

sockaddr_in6结构体概述
- ```c
struct sockaddr_in6 {
    uint16_t sin6_family;   // AF_INET6
    uint16_t sin6_port;     // 端口(网络字节序)
    uint32_t sin6_flowinfo; // flow infomation
    struct in6_addr sin6_addr;  // 地址
    uint32_t sin6_scope_id;     // scope id
};

struct in6_addr {
    unsigned char s6_addr[16];  // IPV6地址
};
- ```

sockaddr_storage结构体概述
- ```c
/* 用于存储IPV4和IPV6地址 */
struct sockaddr_storage {
    sa_family_t ss_family;  // 地址家族
    char __ss_pad1[_SS_PAD1SIZE];
    int64_t __ss_align;
    char __ss_pad2[_SS_PAD2SIZE];
};
- ```

IPV4地址转IPV6的细节
- 协议转换: AF_INET改为AF_INET6
- sockaddr_in改为sockaddr_in6
- sa.sin_addr.s_addr = INADDR_ANY改为sa6.sin6_addr = in6addr_any
- 静态初始化IPV6地址: struct in6_addr ia6 = IN6ADDR_ANY_INIT
- inet_pton替换函数inet_aton,inet_addr()
- inet_ntop替换函数inet_ntoa
- getaddrinfo替换函数gethostbyname
- getnameinfo替换函数gethostbyaddr
- IPV6不再使用INADDR_BROADCAST，请用ipv6-multicast

