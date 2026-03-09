package study.ywork.basis.pears.builder;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/* 继承模式的builder例子 */
public abstract class Pizza {
    final Set<Topping> toppings;

    public enum Topping {
        HAM, MUSHROOM, ONION, PEPPER, SAUSAGE,
    }

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        /* 子类必须实现该类 */
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }
}
