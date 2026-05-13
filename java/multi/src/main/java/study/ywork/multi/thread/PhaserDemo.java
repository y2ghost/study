package study.ywork.multi.thread;

import java.time.LocalTime;
import java.util.concurrent.Phaser;

public class PhaserDemo {
    private static final Phaser phaser = new Phaser() {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            print(String.format("On Advance phase=%s, registered=%s", phase, registeredParties));
            return true;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        print("before running task in main method");
        startTask();
        startTask();
        startTask();
    }

    private static void startTask() throws InterruptedException {
        Thread.sleep(300);
        new Thread(PhaserDemo::taskRun).start();
    }

    private static void taskRun() {
        print("before registering");
        // 注册本线程
        phaser.register();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        print("before arrive");
        // 当前线程将不等待
        phaser.arrive();
        print("after arrive");
    }

    private static void print(String msg) {
        System.out.printf("%-20s: %10s, t=%s, registered=%s, arrived=%s, unarrived=%s phase=%s%n", msg,
            Thread.currentThread().getName(), LocalTime.now(), phaser.getRegisteredParties(),
            phaser.getArrivedParties(), phaser.getUnarrivedParties(), phaser.getPhase());
    }
}
