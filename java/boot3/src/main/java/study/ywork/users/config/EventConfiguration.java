package study.ywork.users.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.users.domain.Event;
import study.ywork.users.repository.EventRepository;

import java.time.LocalDateTime;

@Configuration
public class EventConfiguration {
    @Bean
    ApplicationListener<ApplicationReadyEvent> initEvents(EventRepository eventRepository) {
        return applicationReadyEvent -> {
            Event event = new Event();
            event.setName("Morning Meeting");
            event.setCreatedAt(LocalDateTime.parse("2025-10-12T09:00:00"));
            event.setUpdatedAt(LocalDateTime.parse("2025-10-12T09:00:00"));
            eventRepository.save(event);

            event = new Event();
            event.setName("Lunch Discussion");
            event.setCreatedAt(LocalDateTime.parse("2025-10-12T12:30:00"));
            event.setUpdatedAt(LocalDateTime.parse("2025-10-12T12:30:00"));
            eventRepository.save(event);

            event = new Event();
            event.setName("Evening Review");
            event.setCreatedAt(LocalDateTime.parse("2025-10-12T18:45:00"));
            event.setUpdatedAt(LocalDateTime.parse("2025-10-12T18:45:00"));
            eventRepository.save(event);

            event = new Event();
            event.setName("Next Day Planning");
            event.setCreatedAt(LocalDateTime.parse("2025-10-13T10:00:00"));
            event.setUpdatedAt(LocalDateTime.parse("2025-10-13T10:00:00"));
            eventRepository.save(event);
        };
    }
}
