package study.ywork.quartz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"study.ywork.quartz.repository"})
@EnableTransactionManagement
public class DatabaseConfiguration {
}
