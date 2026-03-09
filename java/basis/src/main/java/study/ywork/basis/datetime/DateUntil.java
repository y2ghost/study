package study.ywork.basis.datetime;

import java.time.LocalDate;

public class DateUntil {
    public static void main(String[] args) {
        LocalDate birth = LocalDate.of(1917, 5, 29);
        LocalDate death = LocalDate.of(1963, 11, 22);
        var lifespan = birth.until(death);
        System.out.println(lifespan);
    }
}
