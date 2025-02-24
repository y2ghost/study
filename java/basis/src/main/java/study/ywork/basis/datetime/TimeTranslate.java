package study.ywork.basis.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeTranslate {
    static String from = "Toronto";
    static String to = "Kolkata";
    static ZoneId fromZone = ZoneId.of("America/" + from);
    static ZoneId toZone = ZoneId.of("Asia/" + to);
    static final String DATE_FORMAT = "yyyy-MM-dd hh:mm a";

    public static void main(String[] args) {
        LocalDateTime when = null;
        if (args.length == 0) {
            when = LocalDateTime.now();
        } else {
            String time = args[0];
            LocalTime localTime = LocalTime.parse(time);
            when = LocalDateTime.of(LocalDate.now(), localTime);
        }
        ZonedDateTime fromZoned = when.atZone(fromZone);
        ZonedDateTime toZoned = fromZoned.withZoneSameInstant(toZone);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
        System.out.printf("Time %s in %s is %s in %s%n", when.format(df), from, toZoned.format(df), to);
    }
}
