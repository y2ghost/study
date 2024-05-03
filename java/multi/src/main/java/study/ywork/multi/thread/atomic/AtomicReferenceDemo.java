package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {
    private static AtomicReference<Double> sum = new AtomicReference<>();

    public static void main(String[] args) throws InterruptedException {
        for (int k = 0; k < 5; k++) {
            sum.set(0d);
            ExecutorService es = Executors.newFixedThreadPool(50);

            for (int i = 1; i <= 50; i++) {
                int finalI = i;
                es.execute(() -> {
                    sum.accumulateAndGet(Math.pow(1.5, finalI), (d1, d2) -> d1 + d2);
                });
            }

            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
            System.out.println(sum.get());
        }
    }
}
