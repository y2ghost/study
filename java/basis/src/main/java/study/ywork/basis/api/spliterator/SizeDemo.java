package study.ywork.basis.api.spliterator;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

public class SizeDemo {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Apple", "Banana", "Orange");
        Spliterator<String> s = list.spliterator();
        s.tryAdvance(System.out::println);
        System.out.println(s.estimateSize());
        System.out.println(s.getExactSizeIfKnown());
    }
}
