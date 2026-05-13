package study.ywork.rabbitmq.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class Worker {
    private static final Logger log = LoggerFactory.getLogger(Worker.class);
    private static final String TASK_QUEUE_NAME = "task_queue";
    private static final int MAX_PREFETCH_COUNT = 1;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = getFactory();
        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            log.info(" [*] Waiting for messages. To exit press CTRL+C");
            // RabbitMQ消息推送不能超过给定MAX_PREFETCH_COUNT
            channel.basicQos(MAX_PREFETCH_COUNT);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info(" [x] Received '{}'", message);
                try {
                    doWork(message);
                } finally {
                    log.info(" [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
            Thread.sleep(1000);
        }
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

