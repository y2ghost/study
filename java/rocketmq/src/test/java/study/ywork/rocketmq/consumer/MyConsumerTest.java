package study.ywork.rocketmq.consumer;

import org.junit.jupiter.api.Test;
import study.ywork.rocketmq.producer.MyProducer;

/**
 * 手工创建topic
 * mqadmin updateTopic -c DefaultCluster -n localhost:9876 -t yy_topic_01
 * mqadmin updateTopic -c DefaultCluster -n localhost:9876 -t yy_topic_02
 */
public class MyConsumerTest {
    @Test
    public void testConsumers() {
        // 首先生产消息
        Thread myProducer1 = new Thread(MyProducer::send);
        Thread myProducer2 = new Thread(MyProducer::asyncSend);
        myProducer1.start();
        myProducer2.start();

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 然后消费消息
        Thread myConsumer1 = new Thread(MyConsumer::recv);
        Thread myConsumer2 = new Thread(MyConsumer::asyncRecv);
        myConsumer1.start();
        myConsumer2.start();

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

