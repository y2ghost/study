import socket

def get_addr_info(hostName):
    ipinfo = "unknown"
    addrs = socket.getaddrinfo(hostName, None,
        socket.AF_INET, socket.SOCK_STREAM)
    for res in addrs:
        af, socktype, proto, canonname, sa = res
        ipinfo = sa[0]

    return ipinfo

def print_host_info():
    host_name = socket.gethostname()
    ip_address = get_addr_info(host_name)
    print(f"host name: {host_name}\nip address: {ip_address}")

def print_remote_host_info():
    remote_host = "www.python.org"
    ip_address = get_addr_info(remote_host)

    try:
        print(f"ip address for '{remote_host}': {ip_address}")
    except socket.error as err_msg:
        print(f"{remote_host}: {err_msg}")


if '__main__' == __name__:
    print_host_info()
    print_remote_host_info()
