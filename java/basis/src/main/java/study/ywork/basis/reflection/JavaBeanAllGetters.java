package study.ywork.basis.reflection;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class JavaBeanAllGetters {
    public static void main(String[] args) throws Exception {
        Object data = LocalDateTime.now();
        BeanInfo info = Introspector.getBeanInfo(data.getClass());

        for (PropertyDescriptor prop : info.getPropertyDescriptors()) {
            Method m = prop.getReadMethod();
            if (m == null) {
                continue;
            }

            String name = m.getName();
            Object[] arguments = null;
            Object value = m.invoke(data, arguments);
            System.out.printf("%s->%s%n", name, value);
        }
    }
}
