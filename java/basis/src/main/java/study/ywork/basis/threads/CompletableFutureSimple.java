package study.ywork.basis.threads;

import java.util.concurrent.CompletableFuture;

class CompletableFutureSimple {
    static String twice(String x) {
        return x + ' ' + x;
    }

    public static void main(String[] args) {
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.thenApply(x -> twice(x))
                .thenAccept(System.out::println);
        cf.complete("Hello");
    }
}
