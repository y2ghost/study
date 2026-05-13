package study.ywork.rabbitmq.client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class RPCClient implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(RPCClient.class);
    private final Connection connection;
    private final Channel channel;
    private static final String REQUEST_QUEUE_NAME = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = getFactory();
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] argv) {
        try (RPCClient fibonacciRpc = new RPCClient()) {
            for (int i = 0; i < 32; i++) {
                String intString = Integer.toString(i);
                log.info(" [x] Requesting fib({})", intString);
                String response = fibonacciRpc.call(intString);
                log.info(" [.] Got '{}'", response);
            }
        } catch (IOException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public String call(String message) throws IOException, InterruptedException, ExecutionException {
        // 唯一标识请求
        final String corrId = UUID.randomUUID().toString();
        // 获取响应数据的队列名称
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", REQUEST_QUEUE_NAME, props, message.getBytes(StandardCharsets.UTF_8));
        final CompletableFuture<String> response = new CompletableFuture<>();
        String cTag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
        });

        String result = response.get();
        channel.basicCancel(cTag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
}
