package study.ywork.multi.thread;

/*
 * Thread.yield(): 让出CPU时间示例
 */
public class ThreadYieldDemo {
    public static void main(String[] args) {
        Task task1 = new Task(true);
        new Thread(task1).start();

        Task task2 = new Task(false);
        new Thread(task2).start();

    }

    private static class Task implements Runnable {
        private final boolean shouldYield;

        public Task(boolean shouldYield) {
            this.shouldYield = shouldYield;
        }

        @Override
        public void run() {
            int counter = 0;
            String threadName = Thread.currentThread().getName();

            System.out.println(threadName + " started.");
            for (int i = 0; i < 1000; i++) {
                counter++;
                if (shouldYield) {
                    // 让出CPU时间
                    Thread.yield();
                }
            }

            System.out.println("counter: " + counter);
            System.out.println(threadName + " ended.");
        }
    }
}
