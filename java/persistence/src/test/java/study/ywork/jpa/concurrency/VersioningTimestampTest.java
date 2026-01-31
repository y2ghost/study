package study.ywork.jpa.concurrency;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.concurrency.versiontimestamp.Item;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;
import java.util.concurrent.Executors;

public class VersioningTimestampTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("concurrencyVersioningTimestamp");
    }

    @Test
    public void firstCommitWins() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            final Long itemId = someItem.getId();
            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            item.setName("New Name");

            Executors.newSingleThreadExecutor().submit(() -> {
                UserTransaction tx1 = tm.getUserTransaction();
                try {
                    tx1.begin();
                    EntityManager em1 = jpa.createEntityManager();

                    Item item1 = em1.find(Item.class, itemId);
                    item1.setName("Other Name");

                    tx1.commit();
                    em1.close();
                } catch (Exception ex) {
                    tm.rollback();
                    throw new RuntimeException("Concurrent operation failure:" + ex, ex);
                }
                return null;
            }).get();

            try {
                tx.commit();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, OptimisticLockException.class);
            }
            em.close();
        } catch (Exception ex) {
            System.err.println(String.format("异常类: %s, 异常原因: %s", ex.getClass(), ex.getMessage()));
        } finally {
            tm.rollback();
        }
    }
}
