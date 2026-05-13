package study.ywork.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MyConsumer {
    public static void recv() {
        try {
            DefaultLitePullConsumer consumer = new
                    DefaultLitePullConsumer("yy_consumer_group1");
            consumer.setNamesrvAddr("localhost:9876");
            // 订阅topic并接收所有消息，第二个属于tag过滤表达式，null或者*表示不过滤
            consumer.subscribe("yy_topic_01", "*");
            consumer.start();
            final List<MessageExt> msgList = consumer.poll();
            if (null != msgList) {
                for (MessageExt msg : msgList) {
                    System.out.println(msg);
                    System.out.println(new String(msg.getBody(), "UTF-8"));
                }
            }

            consumer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void asyncRecv() {
        try {
            DefaultMQPushConsumer consumer = new
                    DefaultMQPushConsumer("yy_consumer_group2");
            consumer.setNamesrvAddr("localhost:9876");
            consumer.subscribe("yy_topic_02", "*");
            consumer.setMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList,
                                                                ConsumeConcurrentlyContext context) {
                    final MessageQueue msgQueue = context.getMessageQueue();
                    System.out.println(msgQueue);
                    for (MessageExt msg : msgList) {
                        try {
                            System.out.println(new String(msg.getBody(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            consumer.start();
            // 等会儿，避免消息没被消费
            Thread.sleep(300000);
            consumer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

