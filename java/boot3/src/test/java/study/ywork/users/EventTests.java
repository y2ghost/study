package study.ywork.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ywork.users.domain.Event;
import study.ywork.users.repository.EventCriteriaRepository;
import study.ywork.users.repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventTests {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCriteriaRepository eventCriteriaRepository;

    @Test
    void testFindByCreatedAtBetween() {
        LocalDate date = LocalDate.of(2025, 10, 12);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<Event> results = eventRepository.findByCreatedAtBetween(startOfDay, endOfDay);
        assertEquals(3, results.size());
    }

    @Test
    void testFindByDateRangeNative() {
        LocalDate date = LocalDate.of(2025, 10, 12);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<Event> results = eventRepository.findByDateRangeNative(startOfDay, endOfDay);
        assertEquals(3, results.size());
    }

    @Test
    void testCriteriaQuery() {
        LocalDate date = LocalDate.of(2025, 10, 12);
        List<Event> results = eventCriteriaRepository.findByCreatedDate(date);
        assertEquals(3, results.size());
    }
}
