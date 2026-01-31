package study.ywork.basis.system;

import java.util.Properties;

public class SystemProperties {
    public static void main(String[] args) {
        Properties props = System.getProperties();
        props.list(System.out);
        System.out.println("--------指定属性获取例子----------");
        System.out.println(System.getProperty("java.home"));
        System.out.println(System.getProperty("java.library.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
    }
}
