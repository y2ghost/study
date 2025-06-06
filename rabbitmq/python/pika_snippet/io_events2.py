import pika

channel = None


def on_connected(connection):
    connection.channel(on_open_callback=on_channel_open)


def on_channel_open(new_channel):
    global channel
    channel = new_channel
    channel.queue_declare(
        queue="test",
        durable=True,
        exclusive=False,
        auto_delete=False,
        callback=on_queue_declared,
    )


def on_queue_declared(frame):
    channel.basic_consume("test", handle_delivery)


def handle_delivery(channel, method, header, body):
    print(body)


def on_close(connection, exception):
    connection.ioloop.stop()


parameters = pika.ConnectionParameters()
connection = pika.SelectConnection(
    on_open_callback=on_connected, on_close_callback=on_close
)

try:
    connection.ioloop.start()
except KeyboardInterrupt:
    connection.close()
    connection.ioloop.start()
