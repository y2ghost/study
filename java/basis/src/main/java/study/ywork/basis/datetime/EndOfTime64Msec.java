package study.ywork.basis.datetime;

import java.util.Date;

public class EndOfTime64Msec {
    public static void main(String[] args) {
        Date endOfTime = new Date(Long.MAX_VALUE);
        System.out.println("Java8 time overflows on " + endOfTime);
    }
}
