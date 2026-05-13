package study.ywork.multi.thread;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ThreadStaticSyncDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 before call " + LocalDateTime.now());
            syncMethod("from thread1");
            System.out.println("thread1 after call " + LocalDateTime.now());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 before call " + LocalDateTime.now());
            syncMethod("from thread2");
            System.out.println("thread2 after call " + LocalDateTime.now());
        });

        thread1.start();
        thread2.start();
    }

    /*
     * 静态方法同步执行，关联的是类上的锁，而非实例
     */
    private static synchronized void syncMethod(String msg) {
        System.out.println("in the sync method " + msg + " " + LocalDateTime.now());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}