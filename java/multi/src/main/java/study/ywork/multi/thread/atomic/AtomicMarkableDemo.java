package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableDemo {
    private static class Test {
        private AtomicMarkableReference<String> ref = new AtomicMarkableReference<>(null, false);

        public void setData(String data) {
            ref.compareAndSet(null, data, false, true);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            Test test = new Test();
            ExecutorService es = Executors.newFixedThreadPool(2);
            es.execute(() -> test.setData("test string"));
            es.execute(() -> {
                boolean[] marked = new boolean[1];
                String s = test.ref.get(marked);
                if (!marked[0] && s != null) {
                    System.out.println("not initialized but data: " + s);
                }
            });
            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
        }

        System.out.println("finished");
    }
}
