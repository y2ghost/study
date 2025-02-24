package study.ywork.basis.reflection;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;

public class JavaCompilerDemo {
    private final static String PACKAGE = "study.ywork.basis.reflection";
    private final static String CLASS = "AnotherDemo";
    private static boolean verbose;

    public static void main(String[] args) throws Exception {
        String source = "package " + PACKAGE + ";\n" +
                "public class " + CLASS + " {\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tString message = (args.length > 0 ? args[0] : \"Hi\")" + ";\n" +
                "\t\tSystem.out.println(message + \" from AnotherDemo\");\n" +
                "\t}\n}\n";
        if (verbose)
            System.out.print("Source to be compiled:\n" + source);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("No default compiler, giving up.");
        }
        Callable<Boolean> compilation =
                compiler.getTask(null, null, null, List.of("-d", "."), null, // <3>
                        List.of(new MySource(CLASS, source)));
        boolean result = compilation.call();
        if (result) {
            System.out.println("Compiled OK");
            Class<?> c = Class.forName(PACKAGE + "." + CLASS);
            System.out.println("Class = " + c);
            Method m = c.getMethod("main", args.getClass());
            System.out.println("Method descriptor = " + m);
            Object[] passedArgs = {args};
            m.invoke(null, passedArgs);
        } else {
            System.out.println("Compilation failed");
        }
    }
}

class MySource extends SimpleJavaFileObject {
    final String source;

    MySource(String fileName, String source) {
        super(URI.create("string:///" + fileName.replace('.', '/') +
                Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
}
