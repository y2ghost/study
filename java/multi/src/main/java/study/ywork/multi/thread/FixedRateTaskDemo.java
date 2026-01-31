package study.ywork.multi.thread;

import java.util.Timer;
import java.util.TimerTask;

public class FixedRateTaskDemo {
    private static int count;

    public static void main(String[] args) {
        System.out.println("Main thread: " + Thread.currentThread());
        Timer timer = new Timer();
        final long start = System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.print("Task invoked - " + (++count) + " - " + (System.currentTimeMillis() - start) + " ms");
                System.out.println(" - " + Thread.currentThread());
            }
        }, 1000, 500);
    }
}
