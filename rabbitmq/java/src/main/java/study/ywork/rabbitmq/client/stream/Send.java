package study.ywork.rabbitmq.client.stream;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static study.ywork.rabbitmq.client.ConnectionUtils.getEnv;

public class Send {
    private static final Logger log = LoggerFactory.getLogger(Send.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Environment environment = getEnv();
        String stream = "hello-java-stream";
        environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();
        Producer producer = environment.producerBuilder().stream(stream).build();
        producer.send(producer.messageBuilder().addData("Hello, World!".getBytes()).build(), null);
        log.info(" [x] 'Hello, World!' message sent");
        log.info(" [x] Press Enter to close the producer...");


        System.in.read();
        Thread.sleep(10000);
        producer.close();
        environment.close();
    }
}
