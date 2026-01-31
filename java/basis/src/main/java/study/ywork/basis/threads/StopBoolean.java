package study.ywork.basis.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StopBoolean {
    protected volatile boolean done = false;

    Runnable r = () -> {
        while (!done) {
            System.out.println("StopBoolean running");
            try {
                Thread.sleep(720);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("StopBoolean finished.");
    };

    public void shutDown() {
        System.out.println("Shutting down...");
        done = true;
    }

    public void doDemo() throws InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(r);
        Thread.sleep(1000 * 5L);
        shutDown();
        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        new StopBoolean().doDemo();
    }
}
