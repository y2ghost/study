import argparse
import sys
import socket
import fcntl
import struct
import array

SIOCGIFADDR = 0x8915

def get_ip_address(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return socket.inet_ntop(socket.AF_INET, fcntl.ioctl(
        s.fileno(), SIOCGIFADDR,
        struct.pack('256s', ifname[:15]))[20:24])

if '__main__' == __name__:
    parser = argparse.ArgumentParser(description='Python networking utils')
    parser.add_argument('--ifname', action="store",
                        dest="ifname", required=True)
    given_args = parser.parse_args()
    ifname = given_args.ifname
    ipstr = get_ip_address(bytes(ifname, encoding="utf8"))
    print(f"Interface [{ifname}] --> IP: {ipstr}")

