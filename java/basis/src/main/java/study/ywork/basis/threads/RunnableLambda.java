package study.ywork.basis.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableLambda {
    private static final ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        threadPool.submit(() -> System.out.println("Hello from a thread"));
        threadPool.shutdown();
    }
}
