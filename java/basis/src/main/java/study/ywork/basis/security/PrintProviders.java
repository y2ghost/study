package study.ywork.basis.security;

import java.security.Provider;
import java.security.Security;
import java.util.Map;

/**
 * Provider实现了Java部分安全，称为提供者 实现DSA、RSA、MD5等算法
 */
public class PrintProviders {
    public static void main(String[] args) {
        for (Provider p : Security.getProviders()) {
            System.out.println(p);
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                System.out.println("\t" + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}
