package study.ywork.basis.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.time.LocalDate;

public class MethodHandleDemo {
    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(LocalDate.class, int.class, int.class, int.class);
        MethodHandle mh = lup.findStatic(LocalDate.class, "of", mt);
        LocalDate endDate = (LocalDate) mh.invokeExact(2014, 6, 10);
        System.out.println("LocalDate from 'of' = " + endDate);
        mt = MethodType.methodType(String.class);
        mh = lup.findVirtual(LocalDate.class, "toString", mt);
        String asString = (String) mh.invokeExact(endDate);
        System.out.println("LocalDate as String is " + asString);
    }
}
