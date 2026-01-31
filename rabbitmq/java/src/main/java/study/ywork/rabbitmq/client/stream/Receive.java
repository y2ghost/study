package study.ywork.rabbitmq.client.stream;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static study.ywork.rabbitmq.client.ConnectionUtils.getEnv;

public class Receive {
    private static final Logger log = LoggerFactory.getLogger(Receive.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Environment environment = getEnv();
        String stream = "hello-java-stream";
        environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();
        Consumer consumer = environment.consumerBuilder()
                .stream(stream)
                .offset(OffsetSpecification.first())
                .messageHandler((unused, message) ->
                        log.info("Received message: {}", new String(message.getBodyAsBinary()))
                ).build();
        log.info(" [x] Press Enter to close the consumer...");
        System.in.read();
        Thread.sleep(10000);
        consumer.close();
        environment.close();
    }
}
