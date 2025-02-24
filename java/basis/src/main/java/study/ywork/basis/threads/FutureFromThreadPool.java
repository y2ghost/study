package study.ywork.basis.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureFromThreadPool {
    static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        double d = 2;
        Callable<Double> computeTotal = () -> d + d;
        Future<Double> future = threadPool.submit(computeTotal);

        while (!future.isDone()) {
            Thread.sleep(100);
        }

        double value = future.get();
        process(value);
        threadPool.shutdown();
    }

    static void process(double d) {
        System.out.println("Processing " + d);
    }
}
