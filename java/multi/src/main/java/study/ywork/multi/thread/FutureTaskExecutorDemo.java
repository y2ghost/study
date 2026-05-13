package study.ywork.multi.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureTaskExecutorDemo {
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

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(futureTask);
        String s = futureTask.get();
        System.out.println(s);
        es.shutdown();
    }
}
