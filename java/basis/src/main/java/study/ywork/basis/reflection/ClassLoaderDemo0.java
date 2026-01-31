package study.ywork.basis.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassLoaderDemo0 extends ClassLoader {
    protected Map<String, Class<?>> cache = new HashMap<>();
    byte[] data /* = ... */;
    int dataLength;

    private byte[] genClassData() {
        if (data == null) {
            throw new NullPointerException("You must initialize the data array");
        }

        if (dataLength != data.length) {
            throw new IllegalArgumentException("data corrupt, " + dataLength + "!=" + data.length);
        }

        byte[] bd = new byte[data.length];
        System.arraycopy(data, 0, bd, 0, bd.length);
        return bd;
    }

    @Override
    public synchronized Class<?> loadClass(String name, boolean resolve) {
        Class<?> c = cache.get(name);
        if (c == null) {
            byte[] tempData = genClassData();
            c = defineClass(name, tempData, 0, tempData.length);
            System.out.println("loadClass: storing " + name + " in cache.");
            cache.put(name, c);
        } else {
            System.out.println("loadClass: found " + name + " in cache.");
        }

        if (resolve) {
            System.out.println("loadClass: About to resolveClass " + name);
            resolveClass(c);
        }

        return c;
    }

    public static void main(String[] argv) {
        System.out.println("ClassLoaderDemo starting");
        ClassLoaderDemo0 loader = new ClassLoaderDemo0();
        Class<?> c;
        Object demo;

        try {
            System.out.println("About to load class  Demo");
            c = loader.loadClass("Demo", true);
            System.out.println("About to instantiate class Demo");
            demo = c.getDeclaredConstructor().newInstance();
            System.out.println("Got Demo class loaded: " + demo);

            Method mi = c.getMethod("test", (Class<?>[]) null);
            mi.invoke(demo, (Object[]) null);
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            System.out.println("Could not run test method");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not run test method");
        }
    }
}
