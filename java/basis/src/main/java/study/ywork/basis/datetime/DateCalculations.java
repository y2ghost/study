package study.ywork.basis.datetime;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class DateCalculations {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weeklyPayDay = now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        System.out.println("Weekly employees' payday is Friday " +
                weeklyPayDay.getMonth() + " " +
                weeklyPayDay.getDayOfMonth());
        LocalDateTime monthlyPayDay = now.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
        System.out.println("Monthly employees are paid on " + monthlyPayDay.toLocalDate());
    }
}
