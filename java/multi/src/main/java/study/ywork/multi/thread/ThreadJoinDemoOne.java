package study.ywork.multi.thread;

/*
 * 等待线程结束示例1
 */
public class ThreadJoinDemoOne {
    public static void main(String[] args) throws InterruptedException {
        Task task1 = new Task();
        Thread thread1 = new Thread(task1);
        thread1.start();
        thread1.join(); // 等待thread1线程结束
        System.out.println("after join");

        Task task2 = new Task();
        new Thread(task2).start();
    }

    private static class Task implements Runnable {
        @Override
        public void run() {
            int counter = 0;
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " started.");

            for (int i = 0; i < 1000; i++) {
                counter += i;
            }

            System.out.println("counter: " + counter);
            System.out.println(threadName + " ended.");
        }
    }
}
