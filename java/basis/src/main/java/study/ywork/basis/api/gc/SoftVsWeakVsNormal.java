package study.ywork.basis.api.gc;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SoftVsWeakVsNormal {
    public static void main(String[] args) {
        List<Reference<MyObject>> references = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 创建 weak reference
            Reference<MyObject> ref = new WeakReference<>(new MyObject("weak " + i));
            references.add(ref);
            // 创建 soft reference
            ref = new SoftReference<>(new MyObject("soft " + i));
            references.add(ref);
            // 创建 normal reference
            new MyObject("normal " + i);
        }

        SoftVsNormal.printReferences(references);
    }
}
