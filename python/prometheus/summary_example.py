import http.server
import time
from prometheus_client import start_http_server
from prometheus_client import Summary

# 记录请求耗时
LATENCY = Summary('hello_world_latency_seconds', '记录请求耗时')

class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        start = time.time()
        self.send_response(200)
        self.end_headers()
        self.wfile.write(b"Hello World")
        LATENCY.observe(time.time() - start)

if __name__ == "__main__":
    start_http_server(8000)
    server = http.server.HTTPServer(('localhost', 8001), MyHandler)
    server.serve_forever()

