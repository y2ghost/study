import pika

_params = pika.URLParameters("amqp://myuser:mypassword@dev.local:5672/myvhost")
connection = pika.BlockingConnection(_params)
__all__ = ["connection"]
