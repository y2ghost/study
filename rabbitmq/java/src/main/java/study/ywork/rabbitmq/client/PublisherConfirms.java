package study.ywork.rabbitmq.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class PublisherConfirms {
    private static final Logger log = LoggerFactory.getLogger(PublisherConfirms.class);
    static final int MESSAGE_COUNT = 50_000;

    static Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = getFactory();
        return factory.newConnection();
    }

    public static void main(String[] args) throws Exception {
        publishMessagesIndividually();
        publishMessagesInBatch();
        handlePublishConfirmsAsynchronously();
    }

    static void publishMessagesIndividually() throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = createConnection(); Channel ch = connection.createChannel()) {
            String queue = UUID.randomUUID().toString();
            ch.queueDeclare(queue, false, false, true, null);
            ch.confirmSelect();
            long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf(i);
                ch.basicPublish("", queue, null, body.getBytes());
                ch.waitForConfirmsOrDie(5_000);
            }
            long end = System.nanoTime();
            log.info("Published {} messages individually in {} ms", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }
    }

    static void publishMessagesInBatch() throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = createConnection(); Channel ch = connection.createChannel()) {
            String queue = UUID.randomUUID().toString();
            ch.queueDeclare(queue, false, false, true, null);
            ch.confirmSelect();
            int batchSize = 100;
            int outstandingMessageCount = 0;
            long start = System.nanoTime();

            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf(i);
                ch.basicPublish("", queue, null, body.getBytes());
                outstandingMessageCount++;

                if (outstandingMessageCount == batchSize) {
                    ch.waitForConfirmsOrDie(5_000);
                    outstandingMessageCount = 0;
                }
            }

            if (outstandingMessageCount > 0) {
                ch.waitForConfirmsOrDie(5_000);
            }
            long end = System.nanoTime();
            log.info("Published {} messages in batch in {} ms", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }
    }

    static void handlePublishConfirmsAsynchronously() throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = createConnection(); Channel ch = connection.createChannel()) {
            String queue = UUID.randomUUID().toString();
            ch.queueDeclare(queue, false, false, true, null);
            ch.confirmSelect();
            ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                            sequenceNumber, true
                    );
                    confirmed.clear();
                } else {
                    outstandingConfirms.remove(sequenceNumber);
                }
            };

            ch.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
                String body = outstandingConfirms.get(sequenceNumber);
                log.error(
                        "Message with body {}} has been nack-ed. Sequence number: {}}, multiple: {}",
                        body, sequenceNumber, multiple
                );
                cleanOutstandingConfirms.handle(sequenceNumber, multiple);
            });

            long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf(i);
                outstandingConfirms.put(ch.getNextPublishSeqNo(), body);
                ch.basicPublish("", queue, null, body.getBytes());
            }

            if (!waitUntil(Duration.ofSeconds(60), outstandingConfirms::isEmpty)) {
                throw new IllegalStateException("All messages could not be confirmed in 60 seconds");
            }

            long end = System.nanoTime();
            log.info("Published {} messages and handled confirms asynchronously in {} ms",
                    MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
        }
    }

    static boolean waitUntil(Duration timeout, BooleanSupplier condition) throws InterruptedException {
        int waited = 0;
        while (!condition.getAsBoolean() && waited < timeout.toMillis()) {
            Thread.sleep(100L);
            waited += 100;
        }
        return condition.getAsBoolean();
    }
}
