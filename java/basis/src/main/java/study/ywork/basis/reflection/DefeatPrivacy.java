package study.ywork.basis.reflection;

import java.lang.reflect.Field;

class ClassWithPrivateField {
    private int p = 42;
    int q = 3;
}

public class DefeatPrivacy {
    public static void main(String[] args) throws Exception {
        new DefeatPrivacy().process();
    }

    private void process() throws IllegalAccessException {
        ClassWithPrivateField x = new ClassWithPrivateField();
        System.out.println(x);
        System.out.println(x.q);
        Class<? extends ClassWithPrivateField> class1 = x.getClass();
        Field[] flds = class1.getDeclaredFields();
        for (Field f : flds) {
            f.setAccessible(true);
            System.out.println(f + "==" + f.get(x));
            f.setAccessible(false);
        }
    }
}
