package study.ywork.multi.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/* 可变线程数的同步屏障功能测试 */
public class PhaserDemo {
    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> System.out.printf("%s running at %d%n", Thread.currentThread().getName(),
            System.currentTimeMillis()));
        tasks.add(() -> System.out.printf("%s running at %d%n", Thread.currentThread().getName(),
            System.currentTimeMillis()));
        runTasks(tasks);
    }

    static void runTasks(List<Runnable> tasks) {
        final Phaser phaser = new Phaser(1);
        for (final Runnable task : tasks) {
            phaser.register();
            Runnable r = () -> {
                try {
                    Thread.sleep(50 + (long)(new Random().nextInt() * 300));
                } catch (InterruptedException ie) {
                    System.out.println("interrupted thread");
                    Thread.currentThread().interrupt();
                }

                phaser.arriveAndAwaitAdvance();
                task.run();
            };

            Executors.newSingleThreadExecutor().execute(r);
        }

        phaser.arriveAndDeregister();
    }
}