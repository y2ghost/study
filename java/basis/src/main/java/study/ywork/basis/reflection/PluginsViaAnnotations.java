package study.ywork.basis.reflection;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginsViaAnnotations {
    public static List<Class<?>> findAnnotatedClasses(String packageName,
                                                      Class<? extends Annotation> annotationClass) throws IOException {
        List<Class<?>> ret = new ArrayList<>();
        List<String> clazzNames = getClasses(packageName);

        for (String element : clazzNames) {
            String clazzName = element.replace('/', '.').replace(".class", "");
            Class<?> c;

            try {
                c = Class.forName(clazzName);
            } catch (ClassNotFoundException ex) {
                System.err.println("Weird: class " + clazzName +
                        " reported in package but gave CNFE: " + ex);
                continue;
            }

            if (c.isAnnotationPresent(annotationClass) &&
                    !ret.contains(c)) {
                ret.add(c);
            }

        }
        return ret;
    }

    private static List<String> getClasses(String packageName) throws IOException {
        String[] clazzNames = ClassesInPackage.getPackageContent(packageName);
        return Arrays.stream(clazzNames).filter(name -> name.endsWith(".class")).toList();
    }

    private PluginsViaAnnotations() {
        // 不做事
    }
}
