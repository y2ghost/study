package study.ywork.multi.thread;

import java.util.concurrent.TimeUnit;

/*
 * 线程优先级设置示例，数值越高优先级越高，这个优先级仅仅是期望值
 * 与操作系统实现相关
 */

public class ThreadPriorityDemo {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.setName("thread 1");
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        Thread thread2 = new MyThread();
        thread2.setName("thread 2");
        thread2.setPriority(Thread.MAX_PRIORITY);
        thread2.start();
    }

    private static class MyThread extends Thread {

        @Override
        public void run() {
            int counter = 0;
            String threadName = Thread.currentThread().getName();

            System.out.println(threadName + " started.");
            for (int i = 0; i < 1000; i++) {
                counter++;
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.err.println(e);
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("counter: " + counter);
            System.out.println(threadName + " ended.");
        }
    }
}
