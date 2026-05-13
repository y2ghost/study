import pika

credentials = pika.PlainCredentials("username", "password")
parameters = pika.ConnectionParameters(credentials=credentials)
parameters2 = pika.URLParameters(
    "amqp://guest:guest@rabbit-server1:5672/%2F?backpressure_detection=t"
)
