package study.ywork.users.redisson.services;

import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RExecutorService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.Callable;

public class ExecutorServiceExamples {
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
        nodeConfig.setExecutorServiceWorkers(Collections.singletonMap("myExecutor", 1));
        RedissonNode node = RedissonNode.create(nodeConfig);
        node.start();

        RExecutorService e = redisson.getExecutorService("myExecutor");
        e.execute(new RunnableTask());
        e.submit(new CallableTask());

        e.shutdown();
        node.shutdown();
        redisson.shutdown();
    }
}
