package study.ywork.basis.datetime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class DateTimeFormatterLM {
    public static void main(String[] args) {
        System.out.println("L       M");
        var then = LocalDate.of(2222, 2, 2);
        IntStream.rangeClosed(1, 5).forEach(i -> {
            var str = "L".repeat(i) + " " + "M".repeat(i);
            var fmt = DateTimeFormatter.ofPattern(str);
            System.out.printf("Length %d (%s) gives %s%n", i, str, fmt.format(then));
        });
    }
}
