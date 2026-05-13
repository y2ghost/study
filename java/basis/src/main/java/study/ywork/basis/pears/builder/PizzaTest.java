package study.ywork.basis.pears.builder;

import static study.ywork.basis.pears.builder.MyPizza.Size.SMALL;
import static study.ywork.basis.pears.builder.Pizza.Topping.HAM;
import static study.ywork.basis.pears.builder.Pizza.Topping.ONION;
import static study.ywork.basis.pears.builder.Pizza.Topping.SAUSAGE;

public class PizzaTest {
    public static void main(String[] args) {
        MyPizza pizza = new MyPizza.Builder(SMALL)
            .addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder()
            .addTopping(HAM).sauceInside().build();
        System.out.println(pizza);
        System.out.println(calzone);
    }
}
