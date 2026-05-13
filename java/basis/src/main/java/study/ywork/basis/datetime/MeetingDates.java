package study.ywork.basis.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class MeetingDates {
    private static int weekOfMonth = 3;
    private static DayOfWeek dayOfWeek = DayOfWeek.WEDNESDAY;

    public static LocalDate getNextMeeting(int weekOfMonth, DayOfWeek dayOfWeek, int meetingsAway) {
        LocalDate now = LocalDate.now();
        LocalDate thisMeeting = now.with(TemporalAdjusters.dayOfWeekInMonth(weekOfMonth, dayOfWeek));

        if (thisMeeting.isBefore(now)) {
            meetingsAway++;
        }

        if (meetingsAway > 0) {
            thisMeeting = thisMeeting.plusMonths(meetingsAway).
                    with(TemporalAdjusters.dayOfWeekInMonth(weekOfMonth, dayOfWeek));
        }
        return thisMeeting;
    }

    public static void main(String[] args) {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        for (int monthAway = 0; monthAway <= 2; monthAway++) {
            LocalDate dt = getNextMeeting(weekOfMonth, dayOfWeek, monthAway);
            System.out.println(dt.format(dfm));
        }
    }
}
