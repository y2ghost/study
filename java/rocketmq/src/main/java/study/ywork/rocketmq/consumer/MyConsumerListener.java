package study.ywork.rocketmq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "yy_topic_01", consumerGroup = "yy_consumer_group1")
public class MyConsumerListener implements RocketMQListener<String> {
    Logger log = LoggerFactory.getLogger(MyConsumerListener.class);

    @Override
    public void onMessage(String s) {
        log.info("Receive Message {}", s);
    }
}

