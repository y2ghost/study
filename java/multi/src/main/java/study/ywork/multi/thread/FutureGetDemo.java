package study.ywork.multi.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class FutureGetDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Long> futureResult = es.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Thread.sleep(1000);
                System.out.println("returning result");
                return ThreadLocalRandom.current().nextLong();
            }
        });
        System.out.println("callable submitted");
        System.out.println("waiting for result");
        // 阻塞直到任务完成得到结果
        Long result = futureResult.get();
        System.out.println(result);
        es.shutdown();
    }
}
