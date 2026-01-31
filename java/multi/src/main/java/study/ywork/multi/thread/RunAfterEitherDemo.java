package study.ywork.multi.thread;

import java.util.concurrent.CompletableFuture;

public class RunAfterEitherDemo {

    public static void main(String[] args) {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            System.out.println("Running 'this' completable");
        });
        CompletableFuture<Void> resultantCf = cf.runAfterEither(getOtherCompletable(), () -> {
            System.out.println("Running after either of the two");
        });
        resultantCf.join();
    }

    private static CompletableFuture<Void> getOtherCompletable() {
        return CompletableFuture.runAsync(() -> {
            threadSleep(15);
            System.out.println("Running other completable");
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
