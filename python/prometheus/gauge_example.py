import http.server
import time
from prometheus_client import start_http_server
from prometheus_client import Gauge

# 适合场景
# 缓存使用大小
# 存活线程数
# 上一次请求处理时间
# 上一分钟每秒请求数
INPROGRESS = Gauge('hello_gauge_inprogress', '处理中的请求数')
# 指标名称建议包含度量单位
LAST = Gauge('hello_gauge_last_time_seconds', '上一次处理时间')

class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        INPROGRESS.inc()
        self.send_response(200)
        self.end_headers()
        self.wfile.write(b"Hello Gauge")
        LAST.set(time.time())
        INPROGRESS.dec()

if __name__ == "__main__":
    start_http_server(8000)
    server = http.server.HTTPServer(('localhost', 8001), MyHandler)
    server.serve_forever()

