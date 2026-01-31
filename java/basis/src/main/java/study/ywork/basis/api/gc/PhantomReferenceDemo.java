package study.ywork.basis.api.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/*
 * 幽灵引用测试例子1
 */
@SuppressWarnings("unused")
public class PhantomReferenceDemo {
    public static void main(String[] args) {
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();
        MyObject myObject1 = new MyObject("phantom");
        Reference<MyObject> ref = new PhantomReference<>(myObject1, referenceQueue);
        System.out.println("ref#get(): " + ref.get());
        MyObject myObject2 = new MyObject("normal");
        // 解除引用
        myObject1 = null;
        myObject2 = null;

        if (checkObjectGced(ref, referenceQueue)) {
            takeAction();
        }

        System.out.println("-- 做一些占用大量内存的工作 --");
        for (int i = 0; i < 10; i++) {
            int[] ints = new int[100000];
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
            }
        }

        if (checkObjectGced(ref, referenceQueue)) {
            takeAction();
        }
    }

    private static boolean checkObjectGced(Reference<MyObject> ref, ReferenceQueue<MyObject> referenceQueue) {
        System.out.println("-- 检查对象垃圾回收是否到期 --");
        Reference<? extends MyObject> polledRef = referenceQueue.poll();
        System.out.println("幽灵引用: " + polledRef);
        boolean gced = polledRef == ref;
        System.out.println("和幽灵引用相同?: " + gced);

        if (polledRef != null) {
            System.out.println("polledRef#get(): " + polledRef.get());
        }

        return gced;
    }

    private static void takeAction() {
        System.out.println("事前清理行动");
    }
}
