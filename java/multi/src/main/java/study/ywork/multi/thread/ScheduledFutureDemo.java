package study.ywork.multi.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ScheduledFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<Long> scheduleFuture = ses.schedule(() -> {
            System.out.println("returning result");
            return ThreadLocalRandom.current().nextLong();
        }, 2, TimeUnit.SECONDS);
        long delay = scheduleFuture.getDelay(TimeUnit.SECONDS);
        System.out.println("task scheduled");
        System.out.println("remaining delay: " + delay);
        Long result = scheduleFuture.get();
        System.out.println(result);
        ses.shutdown();
    }
}
