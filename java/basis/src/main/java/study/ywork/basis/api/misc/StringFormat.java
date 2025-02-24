package study.ywork.basis.api.misc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

// 演示字符串格式化细节
public class StringFormat {
    public static void main(String[] args) {
        // 换行符
        System.out.printf("abc%n456%n");

        // s 和 S 的用法
        System.out.printf("%s%n", "this is my string");
        System.out.printf("%S%n", "this is my string");
        System.out.printf("%s%n", 100);
        System.out.printf("%s%n", new Object());
        System.out.printf("'This is the example of %s.....'%n", "string");

        // b 和 B 的用法
        System.out.printf("%b%n", false);
        System.out.printf("%B%n", false);

        // c 和 C 的用法
        System.out.printf("%c%n", 'a');
        System.out.printf("%C%n", 'a');
        System.out.printf("%c%n", 100);

        // 对齐的用法
        System.out.printf("Result: %20s%n", "example1");
        System.out.printf("Result: %-20s%n", "example2");
        System.out.printf("%-20s result%n", "example2");

        for (int i = 7; i < 300; i += 50) {
            System.out.printf("[Item:%4s  %-4s]%n", i, i * 10);
        }

        // 精度指定x.y: x填充宽度, y总共字节数
        System.out.printf("%2.2s%n", "Hi there!");
        System.out.printf("[%6.4s]%n", "What's up?");
        System.out.printf("[%-6.4s]%n", "What's up?");

        // d 数字类型: byte / short / int / long / BigInteger
        System.out.printf("%d%n", 2);
        System.out.printf("%d%n", (byte) 2);
        System.out.printf("%d%n", 2L);
        System.out.printf("%d%n", BigInteger.valueOf(2L));
        // 前面填充数字0, 不能进行右边填充:%04d
        System.out.printf("%04d%n", 2);

        // 本地化格式化
        System.out.printf(Locale.CHINESE, "%,d%n", 1000000);

        // 添加符号'+'
        for (int i = 1; i < 4; i++) {
            System.out.printf("%+d%n", i);
        }

        // 填充空格
        for (int i = 1; i < 4; i++) {
            System.out.printf("[% d]%n", i);
        }

        // f 浮点数类型: float / double
        System.out.printf("%f%n", 1.33f);
        System.out.printf("%f%n", 1.33d);
        System.out.printf("%f%n", Double.valueOf(1.33d));
        System.out.printf("%f%n", BigDecimal.valueOf(1.33d));
        System.out.printf("[%4.2f]%n", 12.34567);
        System.out.printf("[%5.2f]%n", 12.34567);
        System.out.printf("[%6.2f]%n", 12.34567);
        System.out.printf("[%7.2f]%n", 12.34567);
        System.out.printf("[%-7.2f]%n", 12.34567);
        System.out.printf("[%7.4f]%n", 12.3);
        System.out.printf("[%8.4f]%n", 12.3);

    }

}
