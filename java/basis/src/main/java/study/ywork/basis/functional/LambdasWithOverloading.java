package study.ywork.basis.functional;

import java.util.concurrent.Callable;

public class LambdasWithOverloading {

    void process(Runnable r) {
        dumpOut(r);
        r.run();
    }

    <T> T process(Callable<T> c) throws Exception {
        dumpOut(c);
        return c.call();
    }

    private void dumpOut(Object o) {
        System.out.print(o.getClass().getSimpleName());
        if (o instanceof Callable) {
            System.out.print(" (a Callable)");
        }
        if (o instanceof Runnable) {
            System.out.print(" (a Runnable)");
        }
        System.out.println();
    }

    void tryLambdas() throws Exception {
        String retVal = process(() -> "Hello");

        System.out.println(retVal);

        process(() -> System.out.println("Goodbye"));
    }

    public static void main(String[] args) throws Exception {
        new LambdasWithOverloading().tryLambdas();
    }
}
