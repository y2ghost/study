package study.ywork.basis.api.compiler;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.Arrays;

public class ByteCompilation {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        String className = "Test";
        final JavaByteObject byteObject = new JavaByteObject(className);
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager fileManager = createFileManager(standardFileManager, byteObject);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
                getCompilationUnits(className));

        Boolean isDone = task.call();
        if (!isDone.booleanValue()) {
            diagnostics.getDiagnostics().forEach(System.out::println);
        }

        fileManager.close();
        // 加载编译的类
        final ClassLoader inMemoryClassLoader = createClassLoader(byteObject);
        Class<ITest> test = (Class<ITest>) inMemoryClassLoader.loadClass(className);
        ITest iTest = test.getDeclaredConstructor().newInstance();
        iTest.doSomething();
    }

    private static JavaFileManager createFileManager(StandardJavaFileManager fileManager, JavaByteObject byteObject) {
        return new ForwardingJavaFileManager<StandardJavaFileManager>(fileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
                                                       FileObject sibling) throws IOException {
                return byteObject;
            }
        };
    }

    private static ClassLoader createClassLoader(final JavaByteObject byteObject) {
        return new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                // 没必要去找类，直接获取字节码
                byte[] bytes = byteObject.getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
        };
    }

    public static Iterable<JavaFileObject> getCompilationUnits(String className) {
        JavaStringObject stringObject = new JavaStringObject(className, getSource());
        return Arrays.asList(stringObject);
    }

    public static String getSource() {
        return "public class Test implements study.ywork.basis.api.compiler.ITest{" + "public void doSomething(){"
                + "System.out.println(\"testing\");}}";
    }
}
