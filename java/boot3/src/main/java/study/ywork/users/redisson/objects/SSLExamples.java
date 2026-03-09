package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class SSLExamples {

    public static void main(String[] args) {
        Config config = new Config();

        // 使用SSL连接redis服务
        config.useSingleServer().setAddress("rediss://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);

        RMap<String, String> map = redisson.getMap("test");
        map.put("sslKey", "sslValue");
        String value = map.get("sslKey");
        System.out.println(value);
        redisson.shutdown();
    }
}
