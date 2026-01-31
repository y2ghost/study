package study.ywork.basis.proxy;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class GenericCacheDecorator implements InvocationHandler {
    private static final Object EMPTY = new Object();
    private Map<String, Object> cachedData = new HashMap<>();
    private Object obj;

    private GenericCacheDecorator(Object obj) {
        this.obj = obj;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            for (PropertyDescriptor desc : beanInfo.getPropertyDescriptors()) {
                cachedData.put(desc.getReadMethod().getName(), EMPTY);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <I, T extends I> I decorate(T t, Class<I> interfaceClass) {
        GenericCacheDecorator cacheableDecorator = new GenericCacheDecorator(t);
        return (I) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
            cacheableDecorator);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (cachedData.containsKey(method.getName())) {
            Object o = cachedData.get(method.getName());
            if (o == EMPTY) {
                Object returned = method.invoke(obj, args);
                cachedData.put(method.getName(), returned);
                return returned;
            } else {
                return o;
            }
        }

        return method.invoke(args);
    }

    public static void main(String[] args) {
        MyCacheService cacheService = new MyCacheService();
        CacheService newService = GenericCacheDecorator.decorate(cacheService, CacheService.class);
        System.out.println(newService.getData());
        System.out.println(newService.getData());
    }
}
