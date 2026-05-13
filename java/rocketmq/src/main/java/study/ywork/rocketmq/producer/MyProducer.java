package study.ywork.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class MyProducer {
    // 发送同步消息
    public static void send() {
        try {
            DefaultMQProducer producer = new
                    DefaultMQProducer("yy_producer_group1");
            producer.setNamesrvAddr("localhost:9876");
            producer.setCreateTopicKey("");
            producer.start();
            Message message = new Message(
                    "yy_topic_01",
                    "hello rocketmq".getBytes("UTF-8")
            );
            final SendResult result = producer.send(message);
            System.out.println(result);
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送异步消息
    public static void asyncSend() {
        try {
            DefaultMQProducer producer = new
                    DefaultMQProducer("yy_producer_group2");
            producer.setNamesrvAddr("localhost:9876");
            producer.start();

            for (int i = 0; i < 10; i++) {
                Message message = new Message(
                        "yy_topic_02",
                        ("hello rocketmq " + i).getBytes("UTF-8"));
                producer.send(message, new SendCallback() {
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("发送成功" + sendResult);
                    }

                    public void onException(Throwable throwable) {
                        System.err.println("发送失败" + throwable.getMessage());
                    }
                });
            }

            // 异步发送消息，需要等会儿结束，避免没有收到Broker的确认回复
            Thread.sleep(100000);
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

