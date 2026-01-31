package study.ywork.rabbitmq.client.stream;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static study.ywork.rabbitmq.client.ConnectionUtils.getEnv;

public class OffsetTrackingSend {
    private static final Logger log = LoggerFactory.getLogger(OffsetTrackingSend.class);


    public static void main(String[] args) throws InterruptedException {
        try (Environment environment = getEnv()) {
            String stream = "stream-offset-tracking-java";
            environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(1)).create();

            Producer producer = environment.producerBuilder().stream(stream).build();

            int messageCount = 100;
            CountDownLatch confirmedLatch = new CountDownLatch(messageCount);
            log.info("Publishing {} messages...", messageCount);
            IntStream.range(0, messageCount).forEach(i -> {
                String body = i == messageCount - 1 ? "marker" : "hello";
                producer.send(producer.messageBuilder().addData(body.getBytes(UTF_8)).build(),
                        ctx -> {
                            if (ctx.isConfirmed()) {
                                confirmedLatch.countDown();
                            }
                        });
            });

            boolean completed = confirmedLatch.await(60, TimeUnit.SECONDS);
            log.info("Messages confirmed: {}", completed);
        }
    }
}
