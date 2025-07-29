package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetomany.jointable.Item;
import study.ywork.jpa.model.associations.onetomany.jointable.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class OneToManyJoinTableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToManyJoinTable");
    }

    @Test
    public void storeAndLoadItemUsers() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item someItem = new Item("Some Item");
            em.persist(someItem);
            Item otherItem = new Item("Other Item");
            em.persist(otherItem);

            User someUser = new User("tester");
            someUser.getBoughtItems().add(someItem);
            someItem.setBuyer(someUser);
            someUser.getBoughtItems().add(otherItem);
            otherItem.setBuyer(someUser);
            em.persist(someUser);

            Item unsoldItem = new Item("Unsold Item");
            em.persist(unsoldItem);

            tx.commit();
            em.close();

            Long itemId = someItem.getId();
            Long unsoldItemId = unsoldItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertEquals(item.getBuyer().getUsername(), "tester");
            assertTrue(item.getBuyer().getBoughtItems().contains(item));

            Item item2 = em.find(Item.class, unsoldItemId);
            assertNull(item2.getBuyer());

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}