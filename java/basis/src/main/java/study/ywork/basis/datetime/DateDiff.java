package study.ywork.basis.datetime;

import java.time.LocalDate;
import java.time.Period;

public class DateDiff {
    public static void main(String[] args) {
        LocalDate endOf20thCentury = LocalDate.of(2000, 12, 31);
        LocalDate now = LocalDate.now();

        if (now.getYear() > 2100) {
            System.out.println("The 21st century is over!");
            return;
        }

        Period diff = Period.between(endOf20thCentury, now);
        System.out.printf("The 21st century (up to %s) is %s old%n", now, diff);
        System.out.printf(
                "The 21st century is %d years, %d months and %d days old%n",
                diff.getYears(), diff.getMonths(), diff.getDays());
    }
}