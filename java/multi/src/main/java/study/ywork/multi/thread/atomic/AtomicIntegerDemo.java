package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    private static AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        for (int k = 0; k < 10; k++) {
            num.set(0);
            ExecutorService es = Executors.newFixedThreadPool(2);
            es.execute(() -> {
                for (int i = 0; i < 10000; i++) {
                    num.incrementAndGet();
                }
            });
            es.execute(() -> {
                for (int i = 0; i < 10000; i++) {
                    num.incrementAndGet();
                }
            });

            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
            System.out.println(num.get());
        }
    }
}
