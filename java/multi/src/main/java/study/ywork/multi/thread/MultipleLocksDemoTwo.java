package study.ywork.multi.thread;

import java.util.ArrayList;
import java.util.List;

public class MultipleLocksDemoTwo {
    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();

    public static void main(String[] args) {
        MultipleLocksDemoTwo obj = new MultipleLocksDemoTwo();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                obj.addToList1("thread1 list1 element:" + i);
                obj.addToList2("thread1 list2 element:" + i);
                obj.printLists();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                obj.addToList1("thread2 list1 element:" + i);
                obj.addToList2("thread2 list2 element:" + i);
                obj.printLists();
            }
        });

        thread1.start();
        thread2.start();
    }

    public void addToList1(String s) {
        synchronized (list1) {
            list1.add(s);
        }
    }

    public void addToList2(String s) {
        synchronized (list2) {
            list2.add(s);
        }
    }

    public void printLists() {
        String name = Thread.currentThread().getName();
        synchronized (list1) {
            list1.stream().forEach(l -> System.out.println(name + " : " + l));
        }
        synchronized (list2) {
            list2.stream().forEach(l -> System.out.println(name + " : " + l));
        }
    }
}
