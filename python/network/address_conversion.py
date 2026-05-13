import socket
from binascii import hexlify

def convert_ipv4_address():
    for ip_addr in ['127.0.0.1', '192.168.0.1']:
        packed_ip_addr = socket.inet_pton(socket.AF_INET, ip_addr)
        unpacked_ip_addr = socket.inet_ntop(socket.AF_INET, packed_ip_addr)
        print("IP: %s => Packed: %s, Unpacked: %s" %
              (ip_addr, hexlify(packed_ip_addr), unpacked_ip_addr))

if '__main__' == __name__:
    convert_ipv4_address()
