package study.ywork.multi.thread;

import java.util.concurrent.TimeUnit;

/*
 * 线程终端示例
 */
public class ThreadInterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Task task1 = new Task();
        Thread thread1 = new Thread(task1);
        thread1.start();

        while (true) {
            if (Math.random() > 0.5) {
                thread1.interrupt();
                break;
            }

            TimeUnit.MILLISECONDS.sleep(1);
        }
    }

    private static class Task implements Runnable {
        @Override
        public void run() {
            int c = 0;

            while (true) {
                System.out.println("task running .. " + ++c);
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("interrupted flag=true");
                    terminate();
                    return;
                }

                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.err.println(e);
                    terminate();
                    return;
                }
            }

        }

        private void terminate() {
            System.out.println("Terminating task");
        }
    }
}
