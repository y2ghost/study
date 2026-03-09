package study.ywork.users.redisson.services;

import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.CronSchedule;
import org.redisson.api.RMap;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SchedulerServiceExamples {
    public static class RunnableTask implements Runnable, Serializable {
        @RInject
        private transient RedissonClient redisson;

        @Override
        public void run() {
            RMap<String, String> map = redisson.getMap("myMap");
            map.put("5", "11");
        }
    }

    public static class CallableTask implements Callable<String>, Serializable {
        @RInject
        private transient RedissonClient redisson;

        @Override
        public String call() {
            RMap<String, String> map = redisson.getMap("myMap");
            map.put("1", "2");
            return map.get("3");
        }
    }

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");

        RedissonClient redisson = Redisson.create(config);
        redisson.getKeys().flushall();

        RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
        nodeConfig.setExecutorServiceWorkers(Collections.singletonMap("myExecutor", 5));
        RedissonNode node = RedissonNode.create(nodeConfig);
        node.start();

        RScheduledExecutorService e = redisson.getExecutorService("myExecutor");
        e.schedule(new RunnableTask(), 10, TimeUnit.SECONDS);
        e.schedule(new CallableTask(), 4, TimeUnit.MINUTES);

        e.schedule(new RunnableTask(), CronSchedule.of("10 0/5 * * * ?"));
        e.schedule(new RunnableTask(), CronSchedule.dailyAtHourAndMinute(10, 5));
        e.schedule(new RunnableTask(), CronSchedule.weeklyOnDayAndHourAndMinute(12, 4, Calendar.MONDAY, Calendar.FRIDAY));

        e.shutdown();
        node.shutdown();
        redisson.shutdown();
    }

}
