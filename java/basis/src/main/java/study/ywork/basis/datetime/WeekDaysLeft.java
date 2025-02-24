package study.ywork.basis.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class WeekDaysLeft {
    public static void main(String[] args) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2100, 6, 30);
        Set<DayOfWeek> weekendDays = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        Set<LocalDate> holidays = Set.of(
                LocalDate.of(2023, 4, 7), // Good Friday
                LocalDate.of(2023, 4, 10), // Easter Monday
                LocalDate.of(2023, 4, 28), // PA Day
                LocalDate.of(2023, 5, 22), // Victoria Day
                LocalDate.of(2023, 6, 2), // PA Day
                LocalDate.of(2023, 6, 30)  // PA Day
        );
        Set<LocalDate> marchBreak = new HashSet<>();
        for (int i = 13; i <= 17; i++)
            marchBreak.add(LocalDate.of(2023, 03, i));
        final long weekDaysBetween = startDate.datesUntil(endDate)
                .filter(d -> !weekendDays.contains(d.getDayOfWeek()))
                .filter(d -> !holidays.contains(d))
                .filter(d -> !marchBreak.contains(d))
                .count();
        System.out.println(weekDaysBetween);
    }
}
