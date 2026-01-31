package study.ywork.basis.api.compiler;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;

/*
 * 通过URLClassLoader测试
 */
@SuppressWarnings("unchecked")
public class StringCompilationTwo {
    private static final File outputFile;

    static {
        String outputPath = System.getProperty("user.dir") + File.separatorChar + "build";
        outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            try {
                Files.createDirectory(outputFile.toPath());
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(outputFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
                getCompilationUnits());
        Boolean isDone = task.call();

        if (!isDone.booleanValue()) {
            diagnostics.getDiagnostics().forEach(System.out::println);
        }

        fileManager.close();
        // 加载编译的类
        try (URLClassLoader loader = new URLClassLoader(new URL[]{outputFile.toURI().toURL()})) {
            Class<ITest> test = (Class<ITest>) loader.loadClass("Test");
            ITest iTest = test.getDeclaredConstructor().newInstance();
            iTest.doSomething();
        }
    }

    public static Iterable<JavaFileObject> getCompilationUnits() {
        JavaStringObject stringObject = new JavaStringObject("Test", getSource());
        return Arrays.asList(stringObject);
    }

    public static String getSource() {
        return "public class Test implements study.ywork.basis.api.compiler.ITest{" + "public void doSomething() {"
                + "System.out.println(\"testing\");}}";
    }
}
