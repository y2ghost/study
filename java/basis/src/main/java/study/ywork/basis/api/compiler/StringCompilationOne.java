package study.ywork.basis.api.compiler;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/*
 * 编译字符串代码示例，编译后的CLASS文件在项目根目录，需要添加到CLASSPATH中去
 */
@SuppressWarnings("unchecked")
public class StringCompilationOne {
    public static void main(String[] args)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null,
            getCompilationUnits());
        Boolean isDone = task.call();

        if (!isDone.booleanValue()) {
            diagnostics.getDiagnostics().forEach(System.out::println);
        }

        Class<ITest> test = (Class<ITest>) Class.forName("Test");
        ITest iTest = test.getDeclaredConstructor().newInstance();
        iTest.doSomething();
    }

    public static Iterable<JavaFileObject> getCompilationUnits() {
        JavaStringObject stringObject = new JavaStringObject("Test", getSource());
        return Arrays.asList(stringObject);
    }

    public static String getSource() {
        return "public class Test implements study.ywork.basis.api.compiler.ITest{" + "public void doSomething(){"
            + "System.out.println(\"testing\");}}";
    }
}
