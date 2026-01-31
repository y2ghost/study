package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RedissonClient;

import java.util.Arrays;

public class HyperLogLogExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RHyperLogLog<String> hyperLogLog = redisson.getHyperLogLog("hyperLogLog");
        hyperLogLog.add("1");
        hyperLogLog.add("2");
        hyperLogLog.add("3");
        hyperLogLog.addAll(Arrays.asList("10", "20", "30"));

        RHyperLogLog<String> hyperLogLog1 = redisson.getHyperLogLog("hyperLogLog1");
        hyperLogLog1.add("4");
        hyperLogLog1.add("5");
        hyperLogLog1.add("6");

        RHyperLogLog<String> hyperLogLog2 = redisson.getHyperLogLog("hyperLogLog2");
        hyperLogLog1.add("4");
        hyperLogLog1.add("5");
        hyperLogLog1.add("6");

        hyperLogLog2.mergeWith(hyperLogLog1.getName());
        hyperLogLog2.countWith(hyperLogLog1.getName());

        redisson.shutdown();
    }
}
