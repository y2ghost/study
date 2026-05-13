package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongDemo {
    private static AtomicLong sum = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {
        for (int k = 0; k < 5; k++) {
            sum.set(0);
            ExecutorService es = Executors.newFixedThreadPool(50);

            for (int i = 1; i <= 50; i++) {
                int finalI = i;
                es.execute(() -> {
                    sum.addAndGet((long) Math.pow(2, finalI));
                });
            }

            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
            System.out.println(sum);
        }
    }
}
