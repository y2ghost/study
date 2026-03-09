package study.ywork.multi.thread;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExceptionDemo {
    public static void main(String[] args) {
        runTasksWithExceptionally(2);
        runTasksWithExceptionally(0);
        runTasksWithHandle(2);
        runTasksWithHandle(0);
    }

    private static void runTasksWithExceptionally(int i) {
        System.out.printf("-- input: %s --%n", i);
        CompletableFuture.supplyAsync(() -> 16 / i)
            .thenApply(input -> input + 1)
            .thenApply(input -> input + 3)
            .exceptionally(exception -> {
                System.out.println("in exceptionally");
                System.err.println(exception);
                return 1;
            })
            .thenApply(input -> input * 3)
            .thenAccept(System.out::println);
    }

    private static void runTasksWithHandle(int i) {
        System.out.printf("-- input: %s --%n", i);
        CompletableFuture.supplyAsync(() -> {
            return 16 / i;
        }).handle((input, exception) -> {
            if (exception != null) {
                System.out.println("exception block");
                System.err.println(exception);
                return 1;
            } else {
                System.out.println("increasing input by 2");
                return input + 2;
            }
        }).thenApply(input -> input * 3).thenAccept(System.out::println);
    }
}
