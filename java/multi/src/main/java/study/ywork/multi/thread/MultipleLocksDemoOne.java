package study.ywork.multi.thread;

import java.util.ArrayList;
import java.util.List;

// 多个锁处理的示例1
public class MultipleLocksDemoOne {
    private volatile List<String> list1;
    private volatile List<String> list2;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public static void main(String[] args) {
        MultipleLocksDemoOne obj = new MultipleLocksDemoOne();

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 list1 : " + System.identityHashCode(obj.getList1()));
            System.out.println("thread1 list2 : " + System.identityHashCode(obj.getList2()));
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 list1 : " + System.identityHashCode(obj.getList1()));
            System.out.println("thread2 list2 : " + System.identityHashCode(obj.getList2()));
        });

        thread1.start();
        thread2.start();
    }

    private List<String> getList1() {
        if (list1 == null) {
            synchronized (lock1) {
                if (list1 == null) {
                    list1 = new ArrayList<>();
                }
            }
        }

        return list1;
    }

    private List<String> getList2() {
        if (list2 == null) {
            synchronized (lock2) {
                if (list2 == null) {
                    list2 = new ArrayList<>();
                }
            }
        }

        return list2;
    }
}
