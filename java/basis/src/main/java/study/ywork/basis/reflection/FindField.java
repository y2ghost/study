package study.ywork.basis.reflection;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class FindField {
    public static void main(String[] unused)
            throws NoSuchFieldException, IllegalAccessException {
        FindField gf = new FindField();
        Object o = new YearHolder();
        System.out.println("The value of 'currentYear' is: " +
                gf.intFieldValue(o, "currentYear"));
    }

    int intFieldValue(Object o, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Class<?> c = o.getClass();
        Field fld = c.getField(name);
        int value = fld.getInt(o);
        return value;
    }
}

class YearHolder {
    public int currentYear = LocalDate.now().getYear();
}
