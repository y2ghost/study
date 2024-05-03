package study.ywork.basis.stream;

import java.util.Optional;
import static study.ywork.basis.stream.Dish.MENU;

public class FindingDemo {
    public static void main(String... args) {
        if (isVegetarianFriendlyMenu()) {
            System.out.println("Vegetarian friendly");
        }

        System.out.println(isHealthyMenu());
        System.out.println(isHealthyMenu2());
        Optional<Dish> dish = findVegetarianDish();
        dish.ifPresent(d -> System.out.println(d.getName()));
    }

    private static boolean isVegetarianFriendlyMenu() {
        return MENU.stream().anyMatch(Dish::isVegetarian);
    }

    private static boolean isHealthyMenu() {
        return MENU.stream().allMatch(d -> d.getCalories() < 1000);
    }

    private static boolean isHealthyMenu2() {
        return MENU.stream().noneMatch(d -> d.getCalories() >= 1000);
    }

    private static Optional<Dish> findVegetarianDish() {
        return MENU.stream().filter(Dish::isVegetarian).findAny();
    }
}