package study.ywork.basis.reflection;

import java.util.Random;

public class ClassForName {
    public static void main(String[] av) {
        Class<?> c;
        Object o = null;
        try {
            c = Class.forName("java.util.Random");
            o = c.getConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("That didn't work. " +
                    " Try something else" + e);
        }

        if (o != null && o instanceof Random r) {
            System.out.println("R produced this random # " + r.nextDouble());
        } else {
            throw new IllegalArgumentException("Huh? What gives? Not the right type: " + o);
        }
    }
}
