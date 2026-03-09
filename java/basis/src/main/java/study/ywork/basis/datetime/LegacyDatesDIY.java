package study.ywork.basis.datetime;

import java.text.Format;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class LegacyDatesDIY {
    public static void main(String[] args) {
        Date legacyDate = new Date();
        System.out.println(legacyDate);

        ZoneOffset zoneOffset1 = ZoneOffset.of("-0400");
        long longTime = legacyDate.getTime();
        LocalDateTime convertedDate1 = LocalDateTime.ofEpochSecond(
                longTime / 1000, (int) ((longTime % 1000) * 1000), zoneOffset1);
        System.out.println(convertedDate1);

        TimeZone timeZone = TimeZone.getTimeZone("EST");
        ZoneId zoneId = timeZone.toZoneId();
        System.out.println("EST - > " + zoneId);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MM dd");
        Format legacyFormat = dateTimeFormatter.toFormat();
        System.out.println("Formatted: " + legacyFormat.format(convertedDate1));
    }
}
