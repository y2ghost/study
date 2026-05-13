package study.ywork.multi.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class FutureDoneDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
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
        while (!futureResult.isDone()) {
            Thread.sleep(10);
        }

        System.out.println("task done");
        // 不会阻塞
        Long result = futureResult.get();
        System.out.println(result);
        es.shutdown();
    }
}
