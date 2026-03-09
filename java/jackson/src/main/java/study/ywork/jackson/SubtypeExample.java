package study.ywork.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/*
 * 反序列化为子类
 */
public class SubtypeExample {
    public static void main(String[] args) throws IOException {
        System.out.println("-- Java对象转JSON字符串 --");
        CurrencyRate cr = new CurrencyRate();
        cr.setPair("USD/JPY");
        cr.setRate(BigDecimal.valueOf(109.15).setScale(2, RoundingMode.CEILING));
        System.out.println("Java对象: " + cr);

        ObjectMapper om = new ObjectMapper();
        String s2 = om.writeValueAsString(cr);
        System.out.println("JSON字符串: " + s2);

        System.out.println("-- JSON字符串转Java对象 --");
        CurrencyRate cr2 = om.readValue(s2, CurrencyRate.class);
        System.out.println("原始类型: " + cr2.getRate().getClass());
    }

    @SuppressWarnings("unused")
    private static class CurrencyRate {
        private String pair;

        @JsonDeserialize(as = BigDecimal.class)
        private Number rate;

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }

        public Number getRate() {
            return rate;
        }

        public void setRate(Number rate) {
            this.rate = rate;
        }

        @Override
        public String toString() {
            return "CurrencyRate [pair=" + pair + ", rate=" + rate + "]";
        }
    }
}
