package study.ywork.basis.api.spliterator;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

public class TrySplitDemo {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Apple", "Banana", "Orange");
        Spliterator<String> s1 = list.spliterator();
        Spliterator<String> s2 = s1.trySplit();

        s1.forEachRemaining(System.out::println);
        System.out.println("-- 遍历另一半 --- ");
        s2.forEachRemaining(System.out::println);
    }
}
