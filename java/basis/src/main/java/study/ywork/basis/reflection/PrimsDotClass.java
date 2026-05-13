package study.ywork.basis.reflection;

import java.lang.reflect.Method;

public class PrimsDotClass {
    public static void main(String[] args) {
        Class<?> c = int.class;
        System.out.println(c.getName());
        Method[] methods = c.getMethods();
        System.out.println(c.getName() + " has " + methods.length + " methods");
    }
}
