package study.ywork.multi.thread;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/*
 * 同一个线程可以可以重入，其他线程则会被阻塞
 */
public class ReentrantDemo {
    public static void main(String[] args) throws InterruptedException {
        ReentrantDemo demo = new ReentrantDemo();
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 before call " + LocalDateTime.now());
            demo.syncMethod1("from thread1");
            System.out.println("thread1 after call " + LocalDateTime.now());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 before call " + LocalDateTime.now());
            demo.syncMethod2("from thread2");
            System.out.println("thread2 after call " + LocalDateTime.now());
        });

        thread1.start();
        thread2.start();
    }

    private synchronized void syncMethod1(String msg) {
        System.out.println("in the syncMethod1 " + msg + " " + LocalDateTime.now());
        syncMethod2("from method syncMethod1, reentered call");
    }

    private synchronized void syncMethod2(String msg) {
        System.out.println("in the syncMethod2 " + msg + " " + LocalDateTime.now());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
