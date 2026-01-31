package study.ywork.multi.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* 测试CountDownLatch类功能 */
public class CountDownLatchDemo {
    private static final int NTHREADS = 3;

    public static void main(String[] args) {
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(NTHREADS);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    report("Wait to work");
                    startSignal.await();
                    report("Doing work");
                    Thread.sleep((int) (new Random().nextInt() * 1000));
                    doneSignal.countDown();
                } catch (InterruptedException ie) {
                    System.err.println(ie);
                    Thread.currentThread().interrupt();
                }
            }

            void report(String s) {
                System.out.println(System.currentTimeMillis() + ": " + Thread.currentThread() + ": " + s);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
        for (int i = 0; i < NTHREADS; i++) {
            executor.execute(r);
        }

        System.out.println("Start to wait executor!");
        try {
            System.out.println("Main thread doing something");
            Thread.sleep(1000);
            startSignal.countDown();
            System.out.println("main thread doing await doneSignal");
            doneSignal.await();
            executor.shutdownNow();
        } catch (InterruptedException ie) {
            System.err.println(ie);
            Thread.currentThread().interrupt();
        }
    }
}