package study.ywork.basis.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

public class GetAndInvokeMethod {
    static class X {
        public void work(int i, String s) {
            System.out.printf("Called: i=%d, s=%s%n", i, s);
        }

        public static void main(String[] args) {
            System.out.println("Main.args = " + Arrays.toString(args));
        }
    }

    public static void main(String[] argv) {
        try {
            Class<?> clX = X.class;
            Class<?>[] argTypes = {
                    int.class,
                    String.class
            };

            Method worker = clX.getMethod("work", argTypes);
            Object[] theData = {
                    42,
                    "Chocolate Chips"
            };

            worker.invoke(new X(), theData);
            System.out.println("First Invoke Done");
            final Method m = clX.getMethod("main", String[].class);
            final Object[] args = new Object[1];
            args[0] = "Hello World Of Java".split(" ");

            m.invoke(null, args);
            System.out.println("Second Invoke Done");
        } catch (Exception e) {
            System.err.println("Invoke() failed: " + e);
            e.printStackTrace();
        }
    }
}
