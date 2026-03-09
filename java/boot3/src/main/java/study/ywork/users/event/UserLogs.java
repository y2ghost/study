package study.ywork.users.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_ACTIVATED;
import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_REMOVED;

@Component
public class UserLogs {
    private final Logger log = LoggerFactory.getLogger(UserLogs.class);
    private final RabbitTemplate rabbitTemplate;

    public UserLogs(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @EventListener
    public void userActiveStatusEventHandler(UserActivatedEvent event) {
        log.info("User {} active status: {}", event.getEmail(), event.isActive());
    }

    @Async
    @EventListener
    public void userDeletedEventHandler(UserRemovedEvent event) {
        log.info("User {} DELETED at {}", event.getEmail(), event.getRemoved());
    }


    @Async
    @EventListener
    public void userActiveStatusEventMQHandler(UserActivatedEvent event) {
        this.rabbitTemplate.convertAndSend(USERS_ACTIVATED, event);
        log.info("User {} active status: {}", event.getEmail(), event.isActive());
    }

    @Async
    @EventListener
    public void userDeletedEventMQHandler(UserRemovedEvent event) {
        this.rabbitTemplate.convertAndSend(USERS_REMOVED, event);
        log.info("User {} DELETED at {}", event.getEmail(), event.getRemoved());
    }
}
