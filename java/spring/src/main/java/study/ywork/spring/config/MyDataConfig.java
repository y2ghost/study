package study.ywork.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyDataConfig {
    @Bean
    public MyDataSource getDataSource() {
        return new MyDataSource();
    }
}
