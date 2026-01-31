package study.ywork.basis.functional;


public class ChooseYourMethod {
    static final Runnable[] methods = {
            ChooseYourMethod::method1,
            ChooseYourMethod::method2,
    };

    public static void main(String[] args) {
        for (Runnable r : methods) {
            r.run();
        }

        Runnable r = methods[0];
        r.run();
    }

    static void method1() {
        System.out.println("ChooseYourMethod.method1()");
    }

    static void method2() {
        System.out.println("ChooseYourMethod.method2()");
    }
}
