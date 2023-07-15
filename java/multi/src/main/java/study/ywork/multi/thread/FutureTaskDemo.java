package study.ywork.multi.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            System.out.println("task finished");
            return "The result";
        });

        new Thread(futureTask).start();
        System.out.println("Thread started");
        String s = futureTask.get();
        System.out.println(s);
    }
}
