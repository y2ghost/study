package study.ywork.jackson;

import java.io.IOException;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 反序列化时增加字段信息，应付原始JSON没有相应字段信息时进行补充处理
 */
public class InjectExample {
    public static void main(String[] args) throws IOException {
        String json = "{\"pair\":\"USD/JPY\"}";
        System.out.println("JSON input: " + json);

        InjectableValues.Std iv = new InjectableValues.Std();
        iv.addValue("lastUpdated", LocalDateTime.now());
        iv.addValue(Double.class, 888.5);

        ObjectMapper om = new ObjectMapper();
        om.setInjectableValues(iv);
        CurrencyRate cr = om.readValue(json, CurrencyRate.class);
        System.out.println(cr);
    }

    @SuppressWarnings("unused")
    private static class CurrencyRate {
        private String pair;
        @JacksonInject
        private Double rate;
        @JacksonInject("lastUpdated")
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
