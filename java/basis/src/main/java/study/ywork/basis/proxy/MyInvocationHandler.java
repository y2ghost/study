package study.ywork.basis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

// 动态代理调用处理接口实现函数行为
public class MyInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 不能打印proxy对象，因为调用proxy.toString()函数本质动态代理类调用本方法invoke
        Arrays.stream(Thread.currentThread().getStackTrace()).forEach(System.out::println);
        System.out.println(method);
        System.out.println("调用的接口函数: " + method);
        return null;
    }

    public static void main(String[] args) {
        MyInvocationHandler handler = new MyInvocationHandler();
        CacheService o = (CacheService) Proxy.newProxyInstance(MyInvocationHandler.class.getClassLoader(),
            new Class[] { CacheService.class }, handler);
        o.getData();
    }
}
