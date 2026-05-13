package study.ywork.jpa.share.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class CalendarUtil {
    public static final Calendar TODAY = new GregorianCalendar();
    public static final Calendar TOMORROW = new GregorianCalendar();
    public static final Calendar AFTER_TOMORROW = new GregorianCalendar();

    static {
        TODAY.set(Calendar.HOUR_OF_DAY, 23);
        TODAY.set(Calendar.MINUTE, 59);

        TOMORROW.roll(Calendar.DAY_OF_YEAR, true);
        AFTER_TOMORROW.roll(Calendar.DAY_OF_YEAR, true);
        AFTER_TOMORROW.roll(Calendar.DAY_OF_YEAR, true);
    }

    private CalendarUtil() {
    }
}
