package study.ywork.multi.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class ApplyToEitherDemo {
    public static void main(String[] args) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            threadSleep(100);
            int i = ThreadLocalRandom.current().nextInt(1, 10);
            System.out.println("value to be return from 'this' completable: " + i);
            return i;
        });
        CompletableFuture<Double> resultantCf = cf.applyToEither(getOtherCompletable(), a -> {
            System.out.println("Selected value: " + a);
            return Math.sqrt(a);
        });
        Double d = resultantCf.join();
        System.out.println(d);
    }

    private static CompletableFuture<Integer> getOtherCompletable() {
        return CompletableFuture.supplyAsync(() -> {
            int i = ThreadLocalRandom.current().nextInt(1, 10);
            System.out.println("value to be return from 'other' completable: " + i);
            return i;
        });
    }

    private static void threadSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
