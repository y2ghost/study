import pika

# 文档地址
# https://pika.readthedocs.io/en/stable/intro.html


def on_open(connection):
    pass


def on_close(connection, exception):
    connection.ioloop.stop()


connection = pika.SelectConnection(on_open_callback=on_open, on_close_callback=on_close)

try:
    connection.ioloop.start()
except KeyboardInterrupt:
    connection.close()
    connection.ioloop.start()
