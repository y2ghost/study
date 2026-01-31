package study.ywork.basis.pears.builder;

public class BuilderDemo {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        /* 必须的参数 */
        private final int servingSize;
        private final int servings;
        /* 可选参数，初始化为默认值 */
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public BuilderDemo build() {
            return new BuilderDemo(this);
        }
    }

    private BuilderDemo(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    @Override
    public String toString() {
        return "BuilderDemo [servingSize=" + servingSize + ", servings=" + servings + ", calories=" + calories
            + ", fat=" + fat + ", sodium=" + sodium + ", carbohydrate=" + carbohydrate + "]";
    }

    public static void main(String[] args) {
        BuilderDemo cocaCola = new BuilderDemo.Builder(240, 8)
            .calories(100).sodium(35).carbohydrate(27).build();
        System.out.println(cocaCola);
    }
}