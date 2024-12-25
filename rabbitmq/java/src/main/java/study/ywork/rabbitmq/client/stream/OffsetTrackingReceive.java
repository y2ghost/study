package study.ywork.rabbitmq.client.stream;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.charset.StandardCharsets.UTF_8;
import static study.ywork.rabbitmq.client.ConnectionUtils.getEnv;

public class OffsetTrackingReceive {
    private static final Logger log = LoggerFactory.getLogger(OffsetTrackingReceive.class);

    public static void main(String[] args) throws InterruptedException {
        try (Environment environment = getEnv()) {
            String stream = "stream-offset-tracking-java";
            environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(1)).create();

            OffsetSpecification offsetSpecification = OffsetSpecification.first();
            AtomicLong firstOffset = new AtomicLong(-1);
            AtomicLong lastOffset = new AtomicLong(0);
            AtomicLong messageCount = new AtomicLong(0);
            CountDownLatch consumedLatch = new CountDownLatch(1);
            environment.consumerBuilder().stream(stream)
                    .offset(offsetSpecification)
                    .name("offset-tracking-tutorial")
                    .manualTrackingStrategy().builder()
                    .messageHandler(
                            (ctx, msg) -> {
                                if (firstOffset.compareAndSet(-1, ctx.offset())) {
                                    log.info("First message received.");
                                }
                                if (messageCount.incrementAndGet() % 10 == 0) {
                                    ctx.storeOffset();
                                }
                                String body = new String(msg.getBodyAsBinary(), UTF_8);
                                if ("marker".equals(body)) {
                                    lastOffset.set(ctx.offset());
                                    ctx.storeOffset();
                                    ctx.consumer().close();
                                    consumedLatch.countDown();
                                }
                            })
                    .build();
            log.info("Started consuming...");

            consumedLatch.await(60, TimeUnit.MINUTES);

            log.info("Done consuming, first offset {}, last offset {}",
                    firstOffset.get(), lastOffset.get());
        }
    }
}
