package study.ywork.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;

/*
 * 自定义类型的序列化注册
 */
public class RegisterExample {
    public static void main(String[] args) throws IOException {
        System.out.println("-- Java对象转JSON字符串 --");
        CurrencyRate cr = new CurrencyRate();
        cr.setPair("USD/JPY");
        cr.setRate(109.15);
        cr.setLastUpdated(LocalDateTime.now());
        System.out.println("Java对象: " + cr);

        ObjectMapper om = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDatetimeDeserializer());
        om.registerModule(module);

        String s2 = om.writeValueAsString(cr);
        System.out.println("JSON字符串: " + s2);

        System.out.println("-- JSON字符串转Java对象 --");
        CurrencyRate cr2 = om.readValue(s2, CurrencyRate.class);
        System.out.println("Java对象: " + cr2);
    }

    @SuppressWarnings("unused")
    private static class CurrencyRate {
        private String pair;
        private double rate;
        private LocalDateTime lastUpdated;

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
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
            return "CurrencyRate{" + "pair='" + pair + '\'' + ", rate=" + rate + ", lastUpdated=" + lastUpdated + '}';
        }
    }

    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            try {
                String s = value.format(DATE_FORMATTER);
                System.out.println(provider);
                gen.writeString(s);
            } catch (DateTimeParseException e) {
                System.err.println(e);
                gen.writeString("");
            }
        }
    }

    private static class LocalDatetimeDeserializer extends JsonDeserializer<LocalDateTime> {
        static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            String str = p.getText();
            try {
                System.out.println(ctx);
                return LocalDateTime.parse(str, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.err.println(e);
                return null;
            }
        }
    }
}
