import socket


def find_serv_name():
    protocolname = 'tcp'
    for port in [80, 25]:
        serv_name = socket.getservbyport(port, protocolname)
        print(f"Port: {port} => Service Name: {serv_name}")

    udp_serv_name = socket.getservbyport(53, 'udp')
    print(f"Port: 53 => Service Name: {udp_serv_name}")


if '__main__' == __name__:
    find_serv_name()
