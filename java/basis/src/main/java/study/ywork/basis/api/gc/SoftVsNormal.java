package study.ywork.basis.api.gc;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Soft reference 和 Normal reference对比测试
 */
public class SoftVsNormal {
    public static void main(String[] args) {
        List<Reference<MyObject>> references = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 创建 soft reference
            MyObject myObject = new MyObject("soft " + i);
            Reference<MyObject> ref = new SoftReference<>(myObject);
            references.add(ref);
            // 没有产生任何引用, normal reference
            new MyObject("normal " + i);
        }

        // 查看引用信息
        printReferences(references);
    }

    public static void printReferences(List<Reference<MyObject>> references) {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(() -> {
            try {
                // 休眠一下，以确保垃圾回收工作完成
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
            }

            System.out.println("-- printing references --");
            references.stream().forEach(SoftVsNormal::printReference);
        });
        ex.shutdown();
    }

    public static void printReference(Reference<MyObject> r) {
        System.out.printf("Reference: %s [%s]%n", r.get(), r.getClass().getSimpleName());
    }
}
