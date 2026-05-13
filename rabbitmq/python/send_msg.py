import pika


# 发送的消息也会持久化-服务崩溃会写入磁盘
pikaProperties = pika.BasicProperties(delivery_mode=pika.DeliveryMode.Persistent)
connection = pika.BlockingConnection(pika.ConnectionParameters("localhost"))
channel = connection.channel()
# 队列持久化-服务重启消息队列也会存在
channel.queue_declare(queue="hello", durable=True)

for i in range(5):
    msg = f"hello-rabbit-{i}"
    channel.basic_publish(exchange="",
                          routing_key="hello",
                          body=msg,
                          properties=pikaProperties)
    print(f" [x] sent {msg}")

connection.close()

