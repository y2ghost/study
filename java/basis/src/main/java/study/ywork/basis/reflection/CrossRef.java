package study.ywork.basis.reflection;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

public class CrossRef extends APIFormatter {
    public static void main(String[] argv) throws IOException {
        CrossRef xref = new CrossRef();
        xref.doArgs(argv);
    }

    protected void doClass(Class<?> c) {
        startClass(c);
        try {
            Field[] fields = c.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName));

            for (Field field : fields) {
                if (!Modifier.isPrivate(field.getModifiers())) {
                    putField(field, c);
                }
            }

            Method[] methods = c.getDeclaredMethods();
            Arrays.sort(methods, Comparator.comparing(Method::getName));
            for (Method method : methods) {
                if (!Modifier.isPrivate(method.getModifiers())) {
                    putMethod(method, c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        endClass();
    }

    protected void putField(Field fld, Class<?> c) {
        println(fld.getName() + " field " + c.getName() + " ");
    }

    protected void putMethod(Method method, Class<?> c) {
        String methName = method.getName();
        println(methName + " method " + c.getName() + " ");
    }

    protected void startClass(Class<?> c) {
        // 不做事
    }

    protected void endClass() {
        // 不做事
    }

    protected final void println(String s) {
        System.out.println(s);
    }
}

