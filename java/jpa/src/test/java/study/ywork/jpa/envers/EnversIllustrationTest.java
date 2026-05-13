package study.ywork.jpa.envers;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

class EnversIllustrationTest {
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    protected void setUp() {
        entityManagerFactory = createEntityManagerFactory("study.ywork.jpa");
    }

    @AfterEach
    protected void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void testBasicUsage() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Event("Our very first event!", now()));
        entityManager.persist(new Event("A follow up event", now()));
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Event> result = entityManager.createQuery("from EnverEvent", Event.class).getResultList();

        for (Event event : result) {
            System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
        }

        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Event myEvent = entityManager.find(Event.class, 2L);
        myEvent.setDate(now());
        myEvent.setTitle(myEvent.getTitle() + " (rescheduled)");
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        myEvent = entityManager.find(Event.class, 2L);
        assertEquals("A follow up event (rescheduled)", myEvent.getTitle());
        AuditReader reader = AuditReaderFactory.get(entityManager);
        Event firstRevision = reader.find(Event.class, 2L, 1);
        assertNotEquals(firstRevision.getTitle(), myEvent.getTitle());
        assertNotEquals(firstRevision.getDate(), myEvent.getDate());
        Event secondRevision = reader.find(Event.class, 2L, 2);
        assertEquals(secondRevision.getTitle(), myEvent.getTitle());
        assertEquals(secondRevision.getDate(), myEvent.getDate());
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
