import http.server
import random
from prometheus_client import start_http_server
from prometheus_client import Counter

# 记住计数器只能增加
# 服务重启计数器从0开始会被prometheus自动识别
# 无需客户端持久化保存计数器的值
# 指标名称常见后缀_total, _count, _sum, _bucket
REQUESTS = Counter('hello_counter_total', '记录请求次数')
SALES = Counter('hello_sales_total', '记录销售额值')

class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        REQUESTS.inc()
        cny = random.random()
        SALES.inc(cny)
        self.send_response(200)
        self.end_headers()
        self.wfile.write("增加销售额 {} 元\r\n".format(cny).encode())

if __name__ == "__main__":
    start_http_server(8000)
    server = http.server.HTTPServer(('localhost', 8001), MyHandler)
    server.serve_forever()

