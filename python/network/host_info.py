import socket


def print_host_info():
    host_name = socket.gethostname()
    ip_address = socket.gethostbyname(host_name)
    print(f"host name: {host_name}\nip address: {ip_address}")


def print_remote_host_info():
    remote_host = "www.python.org"
    ip_address = socket.gethostbyname(remote_host)

    try:
        print(f"ip address for '{remote_host}': {ip_address}")
    except socket.error as err_msg:
        print(f"{remote_host}: {err_msg}")


if '__main__' == __name__:
    print_host_info()
    print_remote_host_info()
