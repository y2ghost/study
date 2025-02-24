package study.ywork.rabbitmq.client;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.stream.Environment;

import java.time.Duration;

public class ConnectionUtils {
    public static ConnectionFactory getFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("test");
        factory.setPassword("test123456");
        factory.setVirtualHost("myvhost");
        return factory;
    }

    public static Environment getEnv() {
        return Environment.builder()
                .host("ydoit.dev")
                .username("test")
                .password("test123456")
                .virtualHost("myvhost")
                .requestedHeartbeat(Duration.ofSeconds(5))
                .build();
    }

    private ConnectionUtils() {
        // 不做事儿
    }
}
