package study.ywork.basis.datetime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class DateTimeFormatterLocalized {
    public static void main(String[] args) {
        var dt = ZonedDateTime.now();
        for (Locale l :
                List.of(Locale.CANADA, Locale.FRANCE, Locale.UK, Locale.TAIWAN)) {
            Locale.setDefault(l);
            DateTimeFormatter f =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            System.out.printf(
                    "In Locale %s, date is %s%n", Locale.getDefault(), f.format(dt));
        }
    }
}
