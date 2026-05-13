package study.ywork.multi.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledPeriodDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        AtomicLong al = new AtomicLong(0);
        ScheduledFuture<?> scheduleFuture = ses.scheduleAtFixedRate(() -> {
            System.out.println("running");
            al.incrementAndGet();
        }, 2, 1, TimeUnit.SECONDS);
        System.out.println("task scheduled");
        Thread.sleep(scheduleFuture.getDelay(TimeUnit.MILLISECONDS));
        while (true) {
            Thread.sleep(800);
            long l = al.get();
            System.out.println(l);
            if (l >= 5) {
                System.out.println("cancelling");
                scheduleFuture.cancel(true);
                ses.shutdown();
                break;
            }
        }
    }
}
