package study.ywork.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.ywork.users.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query(
            value = "SELECT * FROM events " +
                    "WHERE created_at >= :startOfDay " +
                    "AND created_at < :endOfDay",
            nativeQuery = true
    )
    List<Event> findByDateRangeNative(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
