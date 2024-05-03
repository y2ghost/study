package study.ywork.multi.thread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

public class AtomicStampedDemo {
    private static class Test {
        private static final int STATE_UNINITIALIZED = -1;
        private static final int STATE_INITIALIZING_IN_PROGRESS = 2;
        private static final int STATE_INITIALIZED = 1;
        private Supplier<String> dataService = getDataService();
        private AtomicStampedReference<String> ref = new AtomicStampedReference<>(null, STATE_UNINITIALIZED);

        public void init() {
            if (ref.compareAndSet(null, null, STATE_UNINITIALIZED, STATE_INITIALIZING_IN_PROGRESS)) {
                String data = dataService.get();
                ref.compareAndSet(null, data, STATE_INITIALIZING_IN_PROGRESS, STATE_INITIALIZED);
            }
        }

        private static Supplier<String> getDataService() {
            return () -> {
                LockSupport.parkNanos(1000);
                return "test string";
            };
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Test test = new Test();
            ExecutorService es = Executors.newFixedThreadPool(2);

            // 读取线程
            es.execute(() -> {
                while (true) {
                    int[] state = { -1 };
                    String data = test.ref.get(state);
                    boolean dataNotNull = data != null;

                    if (state[0] != Test.STATE_INITIALIZED && dataNotNull) {
                        System.out.printf("state: %s, data: %s%n", state, data);
                    }

                    if (state[0] == Test.STATE_INITIALIZED && dataNotNull) {
                        break;
                    }

                    LockSupport.parkNanos(1);
                }
            });

            // 写入线程
            es.execute(test::init);
            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
        }

        System.out.println("finished");
    }
}
