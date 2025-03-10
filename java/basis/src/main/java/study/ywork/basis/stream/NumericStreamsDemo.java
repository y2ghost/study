package study.ywork.basis.stream;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static study.ywork.basis.stream.Dish.MENU;

public class NumericStreamsDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);
        Arrays.stream(numbers.toArray()).forEach(System.out::println);
        int calories = MENU.stream().mapToInt(Dish::getCalories).sum();
        System.out.println("Number of calories:" + calories);

        // max and OptionalInt
        OptionalInt maxCalories = MENU.stream().mapToInt(Dish::getCalories).max();
        int max = 0;

        if (maxCalories.isPresent()) {
            max = maxCalories.getAsInt();
        } else {
            // we can choose a default value
            max = 1;
        }

        System.out.println(max);
        // numeric ranges
        IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
        System.out.println(evenNumbers.count());

        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + (double) b * b) % 1 == 0)
                        .boxed()
                        .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + (double) b * b)}));
        pythagoreanTriples.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
    }

    public static boolean isPerfectSquare(int n) {
        return Math.sqrt(n) % 1 == 0;
    }
}