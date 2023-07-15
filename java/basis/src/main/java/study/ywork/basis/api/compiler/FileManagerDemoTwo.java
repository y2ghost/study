package study.ywork.basis.api.compiler;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;

/* 
 * 使用DiagnosticListener打印诊断信息
 */
public class FileManagerDemoTwo {
    private static final String TEST_FILE = "./samples/FileWithErrors.java";

    public static void main(String[] args) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        MyDiagnosticListener<JavaFileObject> listener = new MyDiagnosticListener<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(listener, null, null);
        File file = new File(TEST_FILE);

        // 文件由StandardFileManager包装到JavaFileObjects中
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file);
        for (JavaFileObject javaFileObject : javaFileObjects) {
            System.out.println(javaFileObject.getClass());
        }

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, listener, null, null, javaFileObjects);
        Boolean isDone = task.call();

        if (isDone.booleanValue()) {
            System.out.println("compilation done");
        }

        fileManager.close();
    }

    private static final class MyDiagnosticListener<S> implements DiagnosticListener<S> {
        @Override
        public void report(Diagnostic<? extends S> diagnostic) {
            System.out.println(diagnostic);
        }
    }
}
