package study.ywork.multi.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/* 可重入的锁测试 */
public class RLDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final ReentrantLock lock = new ReentrantLock();

        class Worker implements Runnable {
            private final String name;

            Worker(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                lock.lock();
                try {
                    if (lock.isHeldByCurrentThread()) {
                        System.out.printf("Thread %s entered critical section.%n", name);
                    }

                    System.out.printf("Thread %s performing work.%n", name);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                        Thread.currentThread().interrupt();
                    }

                    System.out.printf("Thread %s finished working.%n", name);
                } finally {
                    lock.unlock();
                }
            }
        }

        executor.execute(new Worker("ThdA"));
        executor.execute(new Worker("ThdB"));

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.out.println(ex);
            Thread.currentThread().interrupt();
        }

        executor.shutdownNow();
    }
}