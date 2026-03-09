package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.stream.IntStream;

public class AtomicLongArrayDemo {
    private static int len = 5;
    private static AtomicLongArray sumArray = new AtomicLongArray(len);

    public static void main(String[] args) throws InterruptedException {
        for (int k = 0; k < 3; k++) {
            IntStream.range(0, len).forEach(i -> sumArray.set(i, 0L));
            ExecutorService es = Executors.newFixedThreadPool(50);
            for (int b = 2; b < len + 2; b++) {
                for (int i = 1; i <= 10; i++) {
                    int exponent = i;
                    int base = b;
                    es.execute(() -> sumArray.accumulateAndGet(base - 2, (long) Math.pow(base, exponent),
                        (d1, d2) -> d1 + d2));
                }
            }

            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
            IntStream.range(0, len).forEach(i -> System.out.println(sumArray.get(i)));
            System.out.println("-----------");
        }
    }
}
