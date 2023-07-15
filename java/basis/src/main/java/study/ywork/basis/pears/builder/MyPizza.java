package study.ywork.basis.pears.builder;

import java.util.Objects;

public class MyPizza extends Pizza {
    private final Size size;

    public enum Size {
        SMALL, MEDIUM, LARGE,
    }

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public MyPizza build() {
            return new MyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private MyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    @Override
    public String toString() {
        return "MyPizza [size=" + size + ", toppings=" + toppings + "]";
    }
}
