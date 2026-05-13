import pika


connection = pika.BlockingConnection(pika.ConnectionParameters("localhost"))
channel = connection.channel()
channel.queue_declare(queue="hello", durable=True)

def recv_msg(ch, method, properties, body):
    print(f" [x] received {body.decode()}")
    ch.basic_ack(delivery_tag=method.delivery_tag)

channel.basic_consume(queue="hello", on_message_callback=recv_msg)
print(" [*] waiting for messages. To exit press CTRL+C")
channel.start_consuming()

