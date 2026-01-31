import argparse
import http.client

SERVER_HOST = 'www.baidu.com'
SERVER_PATH = '/'


class HttpClient:
    def __init__(self, host):
        self.host = host

    def fetch(self, path):
        client = http.client.HTTPConnection(self.host)
        client.putrequest("GET", path)
        client.putheader("User-Agent", __file__)
        client.putheader("Host", self.host)
        client.putheader("Accept", "*/*")
        client.endheaders()

        try:
            resp = client.getresponse()
        except Exception as e:
            print(f"Client failed error code: {e}")
        else:
            print("Got homepage from %s" % self.host)
            return resp.read()


if '__main__' == __name__:
    parser = argparse.ArgumentParser(description='HTTP Client Example')
    parser.add_argument('--host', action="store", dest="host",
                        default=SERVER_HOST)
    parser.add_argument('--path', action="store", dest="path",
                        default=SERVER_PATH)
    given_args = parser.parse_args()
    host, path = given_args.host, given_args.path
    client = HttpClient(host)
    html = client.fetch(path)
    print(html.decode())

