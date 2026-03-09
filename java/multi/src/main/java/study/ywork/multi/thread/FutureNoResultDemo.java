package study.ywork.multi.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureNoResultDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<?> futureResult = es.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        });
        System.out.println("callable submitted");
        System.out.println("getting result");
        Object o = futureResult.get();
        System.out.println(o);
        es.shutdown();
    }
}
