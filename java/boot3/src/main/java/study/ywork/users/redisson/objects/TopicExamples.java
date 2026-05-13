package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.concurrent.CountDownLatch;

public class TopicExamples {

    public static void main(String[] args) throws InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        CountDownLatch latch = new CountDownLatch(1);

        RTopic topic = redisson.getTopic("topic2");
        topic.addListener(String.class, (channel, msg) -> latch.countDown());

        topic.publish("msg");
        latch.await();

        redisson.shutdown();
    }
}
