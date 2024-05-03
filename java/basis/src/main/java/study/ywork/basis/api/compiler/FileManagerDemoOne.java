package study.ywork.basis.api.compiler;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileManagerDemoOne {
    private static final String TEST_FILE = "./samples/FileWithErrors.java";

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 已有可用的DiagnosticListener实现
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        File file = new File(TEST_FILE);

        // 文件由StandardFileManager包装到JavaFileObjects中
        Iterable<? extends JavaFileObject> javaFileObjects = standardFileManager.getJavaFileObjects(file);
        for (JavaFileObject javaFileObject : javaFileObjects) {
            System.out.println(javaFileObject.getClass());
        }

        JavaCompiler.CompilationTask task = compiler.getTask(null, standardFileManager, diagnostics, null, null,
            javaFileObjects);
        Future<Boolean> future = Executors.newFixedThreadPool(1).submit(task);
        Boolean result = future.get();

        if (result != null && result) {
            System.out.println("Compilation done");
        } else {
            // 诊断信息的打印
            diagnostics.getDiagnostics().forEach(System.out::println);
        }

        standardFileManager.close();
    }
}
