package study.ywork.basis.api.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

public class JavaStringObject extends SimpleJavaFileObject {
    private final String source;

    protected JavaStringObject(String name, String source) {
        super(URI.create("string:///" + name.replace(".", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return source;
    }
}
