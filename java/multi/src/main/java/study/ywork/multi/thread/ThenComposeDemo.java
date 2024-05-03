package study.ywork.multi.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThenComposeDemo {
    public static void main(String[] args) {
        System.out.println("-- Main CompletableFuture: finding a random number x --");
        CompletableFuture<Integer> firstCompletableFuture = CompletableFuture.supplyAsync(() -> {
            int x = ThreadLocalRandom.current().nextInt(1, 5);
            System.out.println("x: " + x);
            return x;
        });

        CompletableFuture<Long> composedCompletableFuture = firstCompletableFuture
            .thenComposeAsync(ThenComposeDemo::getSecondCompletableFuture);
        CompletableFuture<Double> secondComposedCompletableFuture = composedCompletableFuture
            .thenComposeAsync(ThenComposeDemo::getThirdCompletableFuture);
        Double sqrt = secondComposedCompletableFuture.join();
        System.out.println(sqrt);
    }

    private static CompletableFuture<Long> getSecondCompletableFuture(Integer x) {
        System.out.println("-- 2nd CompletableFuture: finding sum of random numbers raised to the power x --");
        return CompletableFuture.supplyAsync(() -> {
            return IntStream.range(1, 5)
                .mapToLong(i -> ThreadLocalRandom.current().nextLong(5, 10))
                .peek(System.out::print)
                .map(y -> (long) Math.pow(y, x))
                .peek(z -> System.out.println(" - " + z))
                .sum();
        });
    }

    private static CompletableFuture<Double> getThirdCompletableFuture(Long sum) {
        System.out.printf("-- 3rd CompletableFuture: finding square root of sum: %s --%n", sum);
        return CompletableFuture.supplyAsync(() -> Math.sqrt(sum));
    }
}
