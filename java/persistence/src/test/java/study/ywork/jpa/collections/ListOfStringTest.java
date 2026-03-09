package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.listofstrings.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class ListOfStringTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("listOfString");
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
            assertEquals(item.getImages().size(), 4);
            assertEquals(item.getImages().get(0), "foo.jpg");
            assertEquals(item.getImages().get(1), "bar.jpg");
            assertEquals(item.getImages().get(2), "baz.jpg");
            assertEquals(item.getImages().get(3), "baz.jpg");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
