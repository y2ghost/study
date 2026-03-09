package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.setofstrings.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class SetOfStringTest extends JpaManager {

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("setOfString");
    }

    @Test
    public void storeLoadCollection() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();

            someItem.getImages().add("foo.jpg");
            someItem.getImages().add("bar.jpg");
            someItem.getImages().add("baz.jpg");
            someItem.getImages().add("baz.jpg");

            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 3);
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
