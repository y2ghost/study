package study.ywork.multi.thread;

import java.util.ArrayList;
import java.util.List;

public class LazyInitBlockDemo {
    // volatile关键字保证双重检查锁技术的正确执行
    private volatile List<String> list;

    public static void main(String[] args) {
        LazyInitBlockDemo obj = new LazyInitBlockDemo();

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 : " + System.identityHashCode(obj.getList()));
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 : " + System.identityHashCode(obj.getList()));
        });

        thread1.start();
        thread2.start();
    }

    // 双重检查锁技术
    private List<String> getList() {
        if (list == null) {
            synchronized (this) {
                if (list == null) {
                    list = new ArrayList<>();
                }
            }
        }

        return list;
    }
}
