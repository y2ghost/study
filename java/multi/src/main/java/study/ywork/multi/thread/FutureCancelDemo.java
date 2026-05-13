package study.ywork.multi.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class FutureCancelDemo {
    public static void main(String[] args) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Long> futureResult = es.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                // 模拟耗时任务
                System.out.println("task started and running");
                Thread.sleep(1000);
                System.out.println("returning result");
                return ThreadLocalRandom.current().nextLong();
            }
        });
        System.out.println("callable submitted");
        System.out.println("canceling task");
        boolean c = futureResult.cancel(true);
        System.out.println(c);
        es.shutdown();
    }
}
