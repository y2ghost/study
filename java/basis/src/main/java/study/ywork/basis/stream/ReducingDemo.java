package study.ywork.basis.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static study.ywork.basis.stream.Dish.MENU;

public class ReducingDemo {
    public static void main(String... args) {
        List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);

        int sum2 = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum2);

        int max = numbers.stream().reduce(0, Integer::max);
        System.out.println(max);

        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        min.ifPresent(System.out::println);

        int calories = MENU.stream().map(Dish::getCalories).reduce(0, Integer::sum);
        System.out.println("Number of calories:" + calories);
    }
}