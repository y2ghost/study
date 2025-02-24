import socket


def convert_integer():
    data = 1234
    print("%s => Long host byte order: %s, network byte order: %s" %
          (data, socket.ntohl(data), socket.htonl(data)))
    print("%s => Short host byte order: %s, network byte order: %s" %
          (data, socket.ntohs(data), socket.htons(data)))


if '__main__' == __name__:
    convert_integer()
