package study.ywork.spring.provider;

import java.time.LocalDateTime;

public class LookupPrototypeBean {
    private String dateTimeString = LocalDateTime.now().toString();

    public String getDateTime() {
        return dateTimeString;
    }
}