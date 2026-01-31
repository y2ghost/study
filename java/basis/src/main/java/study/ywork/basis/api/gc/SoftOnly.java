package study.ywork.basis.api.gc;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class SoftOnly {
    public static void main(String[] args) throws InterruptedException {
        List<Reference<MyObject>> references = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            MyObject myObject = new MyObject("soft " + i);
            Reference<MyObject> ref = new SoftReference<>(myObject);
            references.add(ref);
            // 分配内存慢些
            Thread.sleep(10);
        }

        SoftVsNormal.printReferences(references);
    }
}
