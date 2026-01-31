package study.ywork.basis.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateConversions {
    public static void main(String[] args) {
        Instant epochSec = Instant.ofEpochSecond(1000000000L);
        ZoneId zId = ZoneId.systemDefault();
        ZonedDateTime then = ZonedDateTime.ofInstant(epochSec, zId);
        System.out.println("The epoch was a billion seconds old on " + then);

        long epochSecond = ZonedDateTime.now().toInstant().getEpochSecond();
        System.out.println("Current epoch seconds = " + epochSecond);

        ZonedDateTime here = ZonedDateTime.now();
        ZonedDateTime there = here.withZoneSameInstant(ZoneId.of("Canada/Pacific"));
        System.out.printf("When it's %s here, it's %s in Vancouver%n", here, there);
    }
}
