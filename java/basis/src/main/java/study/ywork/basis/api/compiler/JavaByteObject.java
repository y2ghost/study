package study.ywork.basis.api.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class JavaByteObject extends SimpleJavaFileObject {
    private ByteArrayOutputStream outputStream;

    protected JavaByteObject(String name) {
        super(URI.create("bytes:///" + name + name.replace(".", "/")), Kind.CLASS);
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return outputStream;
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }
}
