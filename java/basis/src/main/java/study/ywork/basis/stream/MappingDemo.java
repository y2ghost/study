package study.ywork.basis.stream;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static study.ywork.basis.stream.Dish.MENU;

public class MappingDemo {
    public static void main(String[] args) {
        List<String> dishNames = MENU.stream().map(Dish::getName).collect(toList());
        System.out.println(dishNames);
        System.out.println();

        List<String> words = Arrays.asList("Hello", "World");
        List<Integer> wordLengths = words.stream().map(String::length).toList();
        System.out.println(wordLengths);
        System.out.println();

        words.stream().flatMap((String line) -> Arrays.stream(line.split(""))).distinct().forEach(System.out::println);
        System.out.println();

        /* 只返回总和能被3整除的数对 */
        List<Integer> numbers1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> numbers2 = Arrays.asList(6, 7, 8);
        List<int[]> pairs = numbers1.stream()
                .flatMap(i -> numbers2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[]{i, j}))
                .toList();
        pairs.forEach(pair -> System.out.println("(" + pair[0] + ", " + pair[1] + ")"));
        System.out.println();
    }
}