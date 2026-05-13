package study.ywork.basis.reflection;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CrossRefXML extends CrossRef {
    public static void main(String[] argv) throws IOException {
        CrossRef xref = new CrossRefXML();
        xref.doArgs(argv);
    }

    protected void startClass(Class<?> c) {
        println("<class><classname>" + c.getName() + "</classname>");
    }

    protected void putField(Field fld, Class<?> c) {
        println("<field>" + fld + "</field>");
    }

    protected void putMethod(Method method, Class<?> c) {
        println("<method>" + method + "</method>");
    }

    protected void endClass() {
        println("</class>");
    }
}
