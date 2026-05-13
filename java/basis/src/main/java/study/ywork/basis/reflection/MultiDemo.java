package study.ywork.basis.reflection;

public class MultiDemo {
    static {
        System.out.println("MultiDemo loaded");
    }

    public MultiDemo() {
        // 不做事儿
    }

    public static void test() {
        System.out.println("MultiDemo.test invoked");
    }

    @Override
    public String toString() {
        return "A MultiDemo object";
    }
}
