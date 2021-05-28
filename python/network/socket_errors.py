import sys
import socket
import argparse


def main():
    parser = argparse.ArgumentParser(description='Socket Error')
    parser.add_argument('--host', action='store',
                        dest='host', required=True)
    parser.add_argument('--port', action='store',
                        dest='port', type=int, required=True)
    given_args = parser.parse_args()
    host = given_args.host
    port = given_args.port

    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    except socket.error as e:
        print(f"Error creating socket: {e}")
        sys.exit(1)

    try:
        s.connect((host, port))
    except socket.gaierror as e:
        print(f"Address related error connecting to server: {e}")
        sys.exit(1)
    except socket.error as e:
        print(f"Connection error: {e}")
        sys.exit(1)

    try:
        s.sendall(b"GET / HTTP/1.1\r\n\r\n")
    except socket.error as e:
        print(f"Error sending data: {e}")
        sys.exit(1)

    while True:
        try:
            buf = s.recv(2048)
        except socket.error as e:
            print(f"Error receiving data: {e}")
            sys.exit(1)

        if not len(buf):
            break

        sys.stdout.write(buf.decode())


if '__main__' == __name__:
    main()
