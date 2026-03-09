package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.redisson.api.stream.StreamReadGroupArgs;

public class StreamExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        String groupName = "testGroup";
        RStream<String, String> stream = redisson.getStream("test");
        stream.createGroup(StreamCreateGroupArgs.name(groupName));

        StreamMessageId id1 = stream.add(StreamAddArgs.entry("1", "1"));
        StreamMessageId id2 = stream.add(StreamAddArgs.entry("2", "2"));

        stream.readGroup(groupName, "consumer1", StreamReadGroupArgs.neverDelivered());

        // 确认消息
        stream.ack(groupName, id1, id2);

        stream.add(StreamAddArgs.entry("3", "3"));
        stream.add(StreamAddArgs.entry("4", "4"));

        stream.readGroup(groupName, "consumer2", StreamReadGroupArgs.neverDelivered());
        stream.listPending(groupName, StreamMessageId.MIN, StreamMessageId.MAX, 100);

        redisson.shutdown();
    }
}
