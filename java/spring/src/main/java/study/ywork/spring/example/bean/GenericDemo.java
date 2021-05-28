package study.ywork.spring.example.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.text.NumberFormat;

/*
 * 泛型对象的依赖注入例子
 */
@Configuration
public class GenericDemo {
    @Bean
    public RateFormatter<Integer> integerRateFormatter() {
        return new RateFormatter<>();
    }

    @Bean
    public RateFormatter<BigDecimal> bigDecimalRateFormatter() {
        return new RateFormatter<>();
    }

    @Bean
    public RateCalculator rateCalculator() {
        return new RateCalculator();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GenericDemo.class);
        RateCalculator bean = context.getBean(RateCalculator.class);
        bean.calculate();
        context.close();
    }

    private static class RateCalculator {
        @Autowired
        private RateFormatter<BigDecimal> formatter;

        public void calculate() {
            BigDecimal rate = BigDecimal.valueOf((Math.random() * 100));
            System.out.println(formatter.format(rate));
        }
    }

    private static class RateFormatter<T extends Number> {
        public String format(T number) {
            NumberFormat format = NumberFormat.getInstance();
            if (number instanceof Integer) {
                format.setMinimumIntegerDigits(0);
            } else if (number instanceof BigDecimal) {
                format.setMinimumIntegerDigits(2);
                format.setMaximumFractionDigits(2);
            }

            return format.format(number);
        }
    }
}
