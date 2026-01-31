package study.ywork.users.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitConfiguration {
    public static final String USERS_EXCHANGE = "USERS";
    public static final String USERS_STATUS_QUEUE = "USER_STATUS";
    public static final String USERS_REMOVED_QUEUE = "USER_REMOVED";
    public static final String USERS_ACTIVATED = "users.activated";
    public static final String USERS_REMOVED = "users.removed";

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(USERS_EXCHANGE);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule())));
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule())));
        return factory;
    }
}
