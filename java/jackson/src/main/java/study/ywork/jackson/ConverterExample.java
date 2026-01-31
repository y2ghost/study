package study.ywork.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ConverterExample {
    public static void main(String[] args) throws IOException {
        System.out.println("-- Java对象转JSON字符串 --");
        CurrencyRate cr = new CurrencyRate();
        cr.setPair("USD/JPY");
        cr.setRate(109.15);
        cr.setLastUpdated(LocalDateTime.now());
        System.out.println("Java对象: " + cr);

        ObjectMapper om = new ObjectMapper();
        String s2 = om.writeValueAsString(cr);
        System.out.println("JSON字符串: " + s2);

        System.out.println("-- JSON字符串转Java对象 --");
        CurrencyRate cr2 = om.readValue(s2, CurrencyRate.class);
        System.out.println("Java对象: " + cr2);
    }

    private static class StringToLocalDatetimeConverter extends StdConverter<String, LocalDateTime> {
        static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        @Override
        public LocalDateTime convert(String value) {
            return LocalDateTime.parse(value, DATE_FORMATTER);
        }
    }

    private static class LocalDateTimeToStringConverter extends StdConverter<LocalDateTime, String> {
        static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        @Override
        public String convert(LocalDateTime value) {
            return value.format(DATE_FORMATTER);
        }
    }

    @SuppressWarnings("unused")
    private static class CurrencyRate {
        private String pair;
        private Double rate;
        @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
        @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
        private LocalDateTime lastUpdated;

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public LocalDateTime getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        @Override
        public String toString() {
            return "CurrencyRate [pair=" + pair + ", rate=" + rate + ", lastUpdated=" + lastUpdated + "]";
        }
    }
}
