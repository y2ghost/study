package study.ywork.users.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import study.ywork.users.domain.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Event> findByCreatedDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);

        cq.select(root).where(
                cb.between(root.get("createdAt"), startOfDay, endOfDay)
        );

        return entityManager.createQuery(cq).getResultList();
    }
}
