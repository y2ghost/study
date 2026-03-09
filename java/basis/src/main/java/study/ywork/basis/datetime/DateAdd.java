package study.ywork.basis.datetime;

import java.time.LocalDate;
import java.time.Period;

public class DateAdd {
    public static void main(String[] av) {
        LocalDate now = LocalDate.now();
        LocalDate then1 = now.plusDays(700);
        Period p = Period.ofDays(700);
        LocalDate then2 = now.plus(p);
        System.out.printf("Seven hundred days from %s is %s or %s%n", now, then1, then2);
    }
}
