import sys
import array
import fcntl
import socket
import struct

SIOCGIFCONF = 0x8912
STUCT_SIZE_32 = 32
STUCT_SIZE_64 = 40
PLATFORM_32_MAX_NUMBER = 2**32
DEFAULT_INTERFACES = 8


def list_interfaces():
    interfaces = []
    max_interfaces = DEFAULT_INTERFACES
    is_64bits = sys.maxsize > PLATFORM_32_MAX_NUMBER
    struct_size = STUCT_SIZE_64 if is_64bits else STUCT_SIZE_32
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    while True:
        nbytes = max_interfaces * struct_size
        interface_names = array.array('B', b'\0' * nbytes)
        sock_info = fcntl.ioctl(
            sock.fileno(),
            SIOCGIFCONF,
            struct.pack('iL', nbytes, interface_names.buffer_info()[0])
        )

        outbytes = struct.unpack('iL', sock_info)[0]
        if outbytes == nbytes:
            max_interfaces *= 2
        else:
            break

    namestr = interface_names.tostring()
    for i in range(0, outbytes, struct_size):
        interfaces.append((namestr[i:i+16].split(b'\0', 1)[0]))

    return interfaces


if '__main__' == __name__:
    interfaces = list_interfaces()
    print("This machine has %s network interfaces: %s." %
          (len(interfaces), interfaces))
