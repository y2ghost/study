package study.ywork.basis.reflection;

import java.lang.reflect.Method;

public class InstanceRunner implements Runnable {
    public InstanceRunner(Class<?> c, String method) {
        targetClass = c;
        methodName = method;
    }

    private Class<?> targetClass;
    private String methodName;

    public void run() {
        try {
            Object o = targetClass.getDeclaredConstructor().newInstance();
            Method m = targetClass.getMethod(methodName);
            m.invoke(o);
        } catch (Exception ex) {
            System.err.println("Caught Exception: " + ex);
        }
    }
}
