package study.ywork.basis.stream;

import java.util.Arrays;
import java.util.List;

public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        MEAT, FISH, OTHER
    }

    @Override
    public String toString() {
        return name;
    }

    protected static final List<Dish> MENU = Arrays.asList(
            new Dish("猪肉", false, 800, Dish.Type.MEAT),
            new Dish("牛肉", false, 700, Dish.Type.MEAT),
            new Dish("鸡肉", false, 400, Dish.Type.MEAT),
            new Dish("薯条", true, 530, Dish.Type.OTHER),
            new Dish("米饭", true, 350, Dish.Type.OTHER),
            new Dish("水果", true, 120, Dish.Type.OTHER),
            new Dish("披萨", true, 550, Dish.Type.OTHER),
            new Dish("大虾", false, 400, Dish.Type.FISH),
            new Dish("三文鱼", false, 450, Dish.Type.FISH));
}