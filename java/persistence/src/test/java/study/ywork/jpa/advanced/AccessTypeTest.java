package study.ywork.jpa.advanced;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class AccessTypeTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }

    @Test
    public void storeLoadAccessType() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some item");
            someItem.setDescription("This is some description.");
            em.persist(someItem);
            tx.commit();
            em.close();

            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertEquals(item.getName(), "AUCTION: Some item");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}