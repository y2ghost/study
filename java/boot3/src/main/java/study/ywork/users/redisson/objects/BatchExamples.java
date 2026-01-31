package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.BatchOptions;
import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;

public class BatchExamples {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RBatch batch = redisson.createBatch(BatchOptions.defaults());
        batch.getMap("test1").fastPutAsync("1", "2");
        batch.getMap("test2").fastPutAsync("2", "3");
        batch.getMap("test3").putAsync("2", "5");
        RFuture<Long> future = batch.getAtomicLong("counter").incrementAndGetAsync();
        batch.getAtomicLong("counter").incrementAndGetAsync();

        future.whenComplete((res, exception) -> {
            // 可以通过RFuture获取执行结果
        });

        // 或是通过显示执行获得结果
        BatchResult<?> res = batch.execute();
        Long counter = (Long) res.getResponses().get(3);
        System.out.println(future.get().equals(counter));

        redisson.shutdown();
    }
}
