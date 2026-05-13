package study.ywork.basis.reflection;

import java.lang.reflect.Constructor;

public class DefaultConstructorDemo {
    public static class PublicClassWithDefaultConstr {
        // 不做事
    }

    public static class PublicClassWithDefaultVisDefaultConstr {
        PublicClassWithDefaultVisDefaultConstr() {
        }
    }

    static class DefaultClassVizDefaultConstr {
        // 不做事
    }

    static class NoDefaultConstructor {
        public NoDefaultConstructor(Object o) {
            System.out.println(o);
        }
    }

    static Class<?>[] classes = {
            PublicClassWithDefaultConstr.class,
            PublicClassWithDefaultVisDefaultConstr.class,
            NoDefaultConstructor.class,
            DefaultClassVizDefaultConstr.class
    };

    public static void main(String[] av) throws Exception {
        for (Class<?> c : classes) {
            System.out.println();
            System.out.println("Starting Class " + c);

            try {
                Constructor<?> con = c.getConstructor(classes);
                System.out.println("Found default con: " + con);
                Object o = con.newInstance(new Object());
                System.out.println("WORKED: " + o);

            } catch (Exception e) {
                System.out.println("FAIL: " + e);
            }
        }
    }
}
