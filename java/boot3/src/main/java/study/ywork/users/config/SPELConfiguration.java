package study.ywork.users.config;

import org.springframework.context.annotation.Bean;
import study.ywork.users.domain.CarPark;
import study.ywork.users.domain.SPELExpression;

/**
 * 学习spring-expression-language用法
 */
public class SPELConfiguration {
    @Bean("carPark")
    public CarPark carPark() {
        return new CarPark();
    }

    @Bean
    public SPELExpression getExpression() {
        return new SPELExpression();
    }
}
