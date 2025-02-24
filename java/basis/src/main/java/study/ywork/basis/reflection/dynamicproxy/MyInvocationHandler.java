package study.ywork.basis.reflection.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class MyInvocationHandler implements InvocationHandler {
    private final Object realObject;

    public MyInvocationHandler(Object realObject) {
        super();
        this.realObject = realObject;
    }

    public Object invoke(Object proxyObject, Method method, Object[] argList)
            throws Throwable {
        System.out.print(
                "Proxy invoking " + method.getName() + "()... ");
        Object ret = method.invoke(realObject, argList);
        System.out.println(" Completed.");
        return ret;
    }
}
