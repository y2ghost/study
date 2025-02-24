package study.ywork.basis.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeFormatterNewLocale {
    public static void main(String[] args) {
        var df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        var now = LocalDateTime.now();
        System.out.println(df.format(now));
        var newdf = df.withLocale(Locale.UK);
        System.out.println(newdf.format(now));
    }
}
