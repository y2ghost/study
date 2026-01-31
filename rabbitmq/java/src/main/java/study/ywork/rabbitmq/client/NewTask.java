package study.ywork.rabbitmq.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class NewTask {
    private static final Logger log = LoggerFactory.getLogger(NewTask.class);
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = getFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 持久化存储消息：首先定义队列持久化
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            String message = String.join(" ", argv);
            // 持久化存储消息：然后定义消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            log.info(" [x] Sent '{}'", message);
        }
    }
}
