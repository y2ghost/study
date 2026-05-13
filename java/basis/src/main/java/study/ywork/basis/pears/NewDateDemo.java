package study.ywork.basis.pears;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class NewDateDemo {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2017, 9, 21);
        int year = date.getYear();
        Month mon = date.getMonth();
        int day = date.getDayOfMonth();
        DayOfWeek dow = date.getDayOfWeek();
        int len = date.lengthOfMonth();
        boolean leap = date.isLeapYear();
        System.out.println("year: " + year + ", mon: "
            + mon + ", day: " + day + ", dow: "
            + dow + ", len: " + len + ", leap: " + leap);
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        System.out.println("today: " + today
            + ", time: " + time + ", hour:"
            + hour + ", minute: " + minute
            + ", second: " + second);
    }
}

