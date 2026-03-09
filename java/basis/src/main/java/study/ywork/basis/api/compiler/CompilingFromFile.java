package study.ywork.basis.api.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;

public class CompilingFromFile {
    private static final String TEST_FILE = "./samples/Test.java";

    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String filePath = new File(TEST_FILE).getAbsolutePath();

        int result = compiler.run(null, null, null, filePath);
        if (result == 0) {
            System.out.println("compilation done");
        }
    }
}
