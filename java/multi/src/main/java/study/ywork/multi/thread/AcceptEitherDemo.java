package study.ywork.multi.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class AcceptEitherDemo {
    public static void main(String[] args) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            int i = ThreadLocalRandom.current().nextInt(1, 10);
            System.out.println("value to be return from 'this' completable: " + i);
            return i;
        });
        CompletableFuture<Void> resultantCf = cf.acceptEither(getOtherCompletable(), a -> {
            System.out.println("Selected value: " + a);
            System.out.println(Math.sqrt(a));
        });
        resultantCf.join();
    }

    private static CompletableFuture<Integer> getOtherCompletable() {
        return CompletableFuture.supplyAsync(() -> {
            threadSleep(100);
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
