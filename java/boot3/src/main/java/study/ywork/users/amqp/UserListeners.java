package study.ywork.users.amqp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import study.ywork.users.event.UserActivatedEvent;
import study.ywork.users.event.UserRemovedEvent;

import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_ACTIVATED;
import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_EXCHANGE;
import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_REMOVED;
import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_REMOVED_QUEUE;
import static study.ywork.users.amqp.UserRabbitConfiguration.USERS_STATUS_QUEUE;

@Component
public class UserListeners {
    private final Logger log = LoggerFactory.getLogger(UserListeners.class);

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(name = USERS_STATUS_QUEUE, durable = "true", autoDelete = "false")
                    , exchange = @Exchange(name = USERS_EXCHANGE, type = "topic"), key = USERS_ACTIVATED))
    public void userStatusEventProcessing(UserActivatedEvent activatedEvent) {
        log.info("[AMQP - Event] Activated Event Received: {}", activatedEvent);
    }

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(name = USERS_REMOVED_QUEUE, durable = "true", autoDelete = "false")
                    , exchange = @Exchange(name = USERS_EXCHANGE, type = "topic"), key = USERS_REMOVED))
    public void userRemovedEventProcessing(UserRemovedEvent removedEvent) {
        log.info("[AMQP - Event] Activated Event Received: {}", removedEvent);
    }

}
