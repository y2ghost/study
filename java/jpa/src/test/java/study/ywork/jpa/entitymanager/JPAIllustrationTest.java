package study.ywork.jpa.entitymanager;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static java.lang.System.out;
import static java.time.LocalDateTime.now;

import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

class JPAIllustrationTest {
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
        inTransaction(entityManager -> {
            entityManager.persist(new Event("Our very first event!", now()));
            entityManager.persist(new Event("A follow up event", now()));
        });

        inTransaction(entityManager -> {
            entityManager.createQuery("select e from Event e", Event.class).getResultList()
                    .forEach(event -> out.println("Event (" + event.getDate() + ") : " + event.getTitle()));
        });
        Assertions.assertTrue(true);
    }

    void inTransaction(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

}
