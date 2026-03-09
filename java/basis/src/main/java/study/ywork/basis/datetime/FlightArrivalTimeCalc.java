package study.ywork.basis.datetime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class FlightArrivalTimeCalc {
    static Duration driveTime = Duration.ofHours(1);

    public static void main(String[] args) {
        LocalDateTime when;
        if (args.length == 0) {
            when = LocalDateTime.now();
        } else {
            String time = args[0];
            LocalTime localTime = LocalTime.parse(time);
            when = LocalDateTime.of(LocalDate.now(), localTime);
        }

        calculateArrivalTime(when);
    }

    public static ZonedDateTime calculateArrivalTime(LocalDateTime takeOffTime) {
        ZoneId torontoZone = ZoneId.of("America/Toronto"),
                londonZone = ZoneId.of("Europe/London");
        ZonedDateTime takeOffTimeZoned =
                ZonedDateTime.of(takeOffTime, torontoZone);
        Duration flightTime =
                Duration.ofHours(5).plus(10, ChronoUnit.MINUTES);
        ZonedDateTime arrivalTimeUnZoned = takeOffTimeZoned.plus(flightTime);
        ZonedDateTime arrivalTimeZoned =
                arrivalTimeUnZoned.toInstant().atZone(londonZone);
        ZonedDateTime phoneTimeHere = arrivalTimeUnZoned.minus(driveTime);

        System.out.println("Flight departure time " + takeOffTimeZoned);
        System.out.println("Flight expected length: " + flightTime);
        System.out.println(
                "Flight arrives there at " + arrivalTimeZoned + " London time.");
        System.out.println("You should phone at " + phoneTimeHere + " Toronto time");
        return arrivalTimeZoned;
    }
}
