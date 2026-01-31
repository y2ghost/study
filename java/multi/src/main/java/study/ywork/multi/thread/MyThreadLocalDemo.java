package study.ywork.multi.thread;

import java.util.Arrays;

public class MyThreadLocalDemo {
    public static void main(String[] args) {
        String[] names = { "one", "two", "three" };
        Arrays.stream(names).forEach(MyThreadLocalDemo::createThread);
    }

    private static void createThread(final String name) {
        new Thread(() -> {
            MyThreadLocal.createMyObject(name);
            for (int i = 0; i < 5; i++) {
                MyThreadLocal.getMyObject().showName();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        }).start();
    }

    private static class MyObject {
        private String name;

        public MyObject(String name) {
            this.name = name;
        }

        public void showName() {
            System.out.printf("MyObject name: %s, thread: %s%n", name, Thread.currentThread().getName());
        }
    }

    private static class MyThreadLocal {
        private static final ThreadLocal<MyObject> threadLocal = new ThreadLocal<>();

        public static MyObject createMyObject(String name) {
            MyObject myObject = new MyObject(name);
            threadLocal.set(myObject);
            return myObject;
        }

        public static MyObject getMyObject() {
            return threadLocal.get();
        }
    }
}
