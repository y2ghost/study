package study.ywork.basis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

//动态拦截方法例子
public class MyInterceptor<T> implements InvocationHandler {
    private T t;

    public MyInterceptor(T t) {
        this.t = t;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("方法调用前: " + method.getName());
        Object result = method.invoke(t, args);
        System.out.println("方法调用后: " + method.getName());
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(T t, Class<? super T> interfaceType) {
        MyInterceptor<T> handler = new MyInterceptor<>(t);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[] { interfaceType }, handler);
    }

    public static void main(String[] args) {
        List<String> list = MyInterceptor.getProxy(new ArrayList<>(), List.class);
        list.add("one");
        list.add("two");
        System.out.println(list);
        list.remove("one");
    }
}
