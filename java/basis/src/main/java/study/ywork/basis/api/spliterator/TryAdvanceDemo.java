package study.ywork.basis.api.spliterator;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

public class TryAdvanceDemo {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Apple", "Banana", "Orange");
        Spliterator<String> s = list.spliterator();
        s.tryAdvance(System.out::println);
        System.out.println(" --- 接着遍历元素");
        s.forEachRemaining(System.out::println);

        System.out.println(" --- 再次尝试tryAdvance");
        boolean b = s.tryAdvance(System.out::println);
        System.out.println("元素存在？: " + b);

    }
}
