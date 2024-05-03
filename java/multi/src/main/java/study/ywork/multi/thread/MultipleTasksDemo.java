package study.ywork.multi.thread;

import java.util.Timer;
import java.util.TimerTask;

public class MultipleTasksDemo {
    public static void main(String[] args) {
        System.out.println("Main thread: " + Thread.currentThread());
        Timer timer = new Timer();
        final long start = System.currentTimeMillis();

        final TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                System.out.print("Task1 invoked: " + (System.currentTimeMillis() - start) + " ms");
                System.out.println(" - " + Thread.currentThread());

                try {
                    // 任务执行应该尽可能的快，此处模拟长时间运行
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        };

        final TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                System.out.print("Task2 invoked : " + (System.currentTimeMillis() - start) + " ms");
                System.out.println(" - " + Thread.currentThread());
                timer.cancel();
            }
        };

        timer.schedule(timerTask1, 1000);
        timer.schedule(timerTask2, 2000);
    }
}
