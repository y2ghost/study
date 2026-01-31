package study.ywork.basis.api.gc;

import java.lang.ref.Cleaner;

/*
 * Java垃圾收集-在目标对象到期进行垃圾收集时，使用Java9 Cleaner接收通知
 */
@SuppressWarnings("unused")
public class CleanerDemo {
    public static void main(String[] args) {
        Cleaner cleaner = Cleaner.create();
        for (int i = 0; i < 10; i++) {
            String id = Integer.toString(i);
            MyObject myObject = new MyObject(id);
            cleaner.register(myObject, new CleanerRunnable(id));
        }

        // myObjects不再可访问，做一些其他占用大量内存的工作
        for (int i = 1; i <= 10000; i++) {
            int[] a = new int[10000];
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class CleanerRunnable implements Runnable {
        private String id;

        public CleanerRunnable(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.printf("MyObject with id %s is in gc%n", id);
        }
    }
}
