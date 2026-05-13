package study.ywork.multi.thread;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThenCombineDemo {
    public static void main(String[] args) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            int x = ThreadLocalRandom.current().nextInt(1, 5);
            System.out.println("Main stage: " + x);
            return x;
        });

        CompletableFuture<Double> finalCf = cf.thenCombine(getOther(),
            (x, ints) -> Arrays.stream(ints).mapToDouble(i -> Math.pow(i, x)).sum());
        Double d = finalCf.join();
        System.out.println(d);
    }

    private static CompletableFuture<int[]> getOther() {
        return CompletableFuture.supplyAsync(() -> {
            int[] ints = IntStream.range(1, 5).map(i -> ThreadLocalRandom.current().nextInt(5, 10)).toArray();
            System.out.println("Other stage: " + Arrays.toString(ints));
            return ints;
        });
    }
}
