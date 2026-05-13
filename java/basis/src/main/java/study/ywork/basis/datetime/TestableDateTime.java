package study.ywork.basis.datetime;

import java.time.Clock;
import java.time.LocalDateTime;

public class TestableDateTime {
    private static Clock clock = Clock.systemDefaultZone();

    public static void main(String[] args) {
        System.out.println("It is now " + LocalDateTime.now(clock));
    }

    public static void setClock(Clock clock) {
        TestableDateTime.clock = clock;
    }
}
