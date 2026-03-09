package study.ywork.basis.api.spliterator;

import java.util.Arrays;
import java.util.List;

public class BasicDemo {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Apple", "Banana", "Orange");
        System.out.println(list.spliterator().characteristics());

        for (String s : list) {
            System.out.println(s);
        }
    }
}
