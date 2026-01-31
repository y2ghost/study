package study.ywork.basis.datetime;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class WeekOfYear {
    public static void main(String[] args) {
        int year = Integer.parseInt(args[0]);
        int month = Integer.parseInt(args[1]);
        int day = Integer.parseInt(args[2]);
        LocalDate date = LocalDate.of(year, month, day);
        int week = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        System.out.printf("%4d-%02d-%02d was in week %d of %d.", year, month, day, week, year);
    }
}
