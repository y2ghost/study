package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.Collections;

public class ScriptExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RBucket<String> bucket = redisson.getBucket("foo");
        bucket.set("bar");

        RScript script = redisson.getScript(StringCodec.INSTANCE);

        // 只读模式执行脚本
        String result = script.eval(RScript.Mode.READ_ONLY,
                "return redis.call('get', 'foo')",
                RScript.ReturnType.VALUE);
        System.out.println("result " + result);

        // 使用SHA执行脚本
        String sha1 = script.scriptLoad("return redis.call('get', 'foo')");
        result = redisson.getScript().evalSha(RScript.Mode.READ_ONLY,
                sha1, RScript.ReturnType.VALUE, Collections.emptyList());
        System.out.println("result " + result);

        redisson.shutdown();
    }
}
