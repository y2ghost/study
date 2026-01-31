package study.ywork.basis.datetime;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleSlider {
    public static void main(String[] args) {
        LocalDate start = LocalDate.parse("2020-12-06");
        LocalDate end = LocalDate.parse("2020-12-18");

        if (start.equals(end)) {
            throw new IllegalArgumentException("Your days will get you nowhere!");
        }

        boolean reverse = end.compareTo(start) < 0;
        final int hoursIncrement = reverse ? -1 : +1;
        final int daysIncrement = reverse ? -1 : +1;
        LocalTime breakfast = LocalTime.of(07, 00);
        LocalTime lunch = LocalTime.of(12, 00);
        LocalTime dinner = LocalTime.of(17, 00);
        LocalTime bed = LocalTime.of(21, 30);
        String format = "%-10s  %-10s %s  %s  %s\n";
        System.out.printf(format, "Day", "Breakfast", "Lunch", "Dinner", "Bed");

        for (LocalDate day = start; day.compareTo(end) <= 0; day = day.plusDays(daysIncrement)) {
            System.out.printf(format, day, breakfast, lunch, dinner, bed);
            breakfast = breakfast.plusHours(hoursIncrement);
            lunch = lunch.plusHours(hoursIncrement);
            dinner = dinner.plusHours(hoursIncrement);
            bed = bed.plusHours(hoursIncrement);
        }
    }
}
