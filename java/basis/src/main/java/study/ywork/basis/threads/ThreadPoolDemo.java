package study.ywork.basis.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolDemo {
    private static final int HOW_MANY = 5;
    private static boolean done = false;

    public static void main(String[] args) throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(HOW_MANY);
        List<Future<Integer>> futures = new ArrayList<>(HOW_MANY);

        for (int i = 0; i < HOW_MANY; i++) {
            Future<Integer> f = pool.submit(new DemoRunnable(i));
            System.out.println("Got 'Future' of type " + f.getClass());
            futures.add(f);
        }

        Thread.sleep(3 * 1000L);
        done = true;
        for (Future<Integer> f : futures) {
            System.out.println("Result " + f.get());
        }

        pool.shutdown();
    }

    static class DemoRunnable implements Callable<Integer> {
        int time;
        int numRuns;

        DemoRunnable(int t) {
            time = t;
        }

        @Override
        public Integer call() {
            while (!done) {
                System.out.println("Running " + Thread.currentThread());
                ++numRuns;
            }

            System.out.println("Stopping " + this);
            return numRuns;
        }
    }
}
