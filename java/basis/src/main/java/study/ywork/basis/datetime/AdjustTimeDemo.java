package study.ywork.basis.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * Temporal接口定义了日期和时间的表示形式
 * 常见实现类有LocalDate、LocalDateTime、Instant
 * TemporalAdjuster接口定义了调整时间对象的策略，属于函数接口
 * TemporalAdjusters类预定了很多策略函数实现
 */
public class AdjustTimeDemo {
    private static final LocalDate TEST_DATE = LocalDate.of(2024, 12, 16);

    public static void main(String[] args) {
        nextSunday();
        fourteenDaysAfterDate();
        nextWorkingDay();
        functionNextWorkingDay();
    }

    private static void nextSunday() {
        LocalDate nextSunday = TEST_DATE.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        System.out.println(nextSunday);
    }

    private static void fourteenDaysAfterDate() {
        TemporalAdjuster temporalAdjuster = t -> t.plus(Period.ofDays(14));
        LocalDate result = TEST_DATE.with(temporalAdjuster);
        System.out.println(result);
    }

    private static TemporalAdjuster NEXT_WORKING_DAY = TemporalAdjusters.ofDateAdjuster(date -> {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int daysToAdd;
        if (dayOfWeek == DayOfWeek.FRIDAY)
            daysToAdd = 3;
        else if (dayOfWeek == DayOfWeek.SATURDAY)
            daysToAdd = 2;
        else
            daysToAdd = 1;
        return date.plusDays(daysToAdd);
    });

    private static void nextWorkingDay() {
        TemporalAdjuster temporalAdjuster = NEXT_WORKING_DAY;
        LocalDate result = TEST_DATE.with(temporalAdjuster);
        System.out.println(result);
    }

    private static Temporal adjustInto(Temporal temporal) {
        DayOfWeek dayOfWeek = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
        int daysToAdd;

        if (dayOfWeek == DayOfWeek.FRIDAY) {
            daysToAdd = 3;
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            daysToAdd = 2;
        } else {
            daysToAdd = 1;
        }

        return temporal.plus(daysToAdd, ChronoUnit.DAYS);
    }

    private static void functionNextWorkingDay() {
        LocalDate nextWorkingDay = TEST_DATE.with(AdjustTimeDemo::adjustInto);
        System.out.println(nextWorkingDay);
    }
}
