package study.ywork.rocketmq.producer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ywork.rocketmq.MQBootApp;

/**
 * 手工创建topic
 * mqadmin updateTopic -c DefaultCluster -n localhost:9876 -t yy_topic_01
 * mqadmin updateTopic -c DefaultCluster -n localhost:9876 -t yy_topic_02
 */
@SpringBootTest(classes = MQBootApp.class)
public class MyProducerTest {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testRocketMQTemplate() {
        rocketMQTemplate.convertAndSend("yy_topic_01","hello RocketMQTemplate");

        // 等会儿，需要等待Broker的回复
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
