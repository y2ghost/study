package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanDemo {
    private static AtomicBoolean init = new AtomicBoolean();

    public static void init() {
        if (init.compareAndSet(false, true)) {
            System.out.println("initializing");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int c = 10;
        ExecutorService es = Executors.newFixedThreadPool(c);

        for (int i = 0; i < c; i++) {
            es.execute(AtomicBooleanDemo::init);
        }

        es.shutdown();
        es.awaitTermination(10, TimeUnit.MINUTES);
    }
}
