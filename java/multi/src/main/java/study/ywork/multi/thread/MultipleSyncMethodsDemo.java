package study.ywork.multi.thread;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/*
 * 内在锁定在对象上，而不在方法上
 */
public class MultipleSyncMethodsDemo {
    public static void main(String[] args) throws InterruptedException {
        MultipleSyncMethodsDemo demo = new MultipleSyncMethodsDemo();
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
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    private synchronized void syncMethod2(String msg) {
        System.out.println("in the syncMethod2 " + msg + " " + LocalDateTime.now());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
