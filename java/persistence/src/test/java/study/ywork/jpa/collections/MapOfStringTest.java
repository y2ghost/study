package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.mapofstrings.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class MapOfStringTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mapOfString");
    }

    @Test
    public void storeLoadCollection() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();

            someItem.getImages().put("foo.jpg", "Foo");
            someItem.getImages().put("bar.jpg", "Bar");
            someItem.getImages().put("baz.jpg", "WRONG!");
            someItem.getImages().put("baz.jpg", "Baz");

            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 3);
            assertEquals(item.getImages().get("foo.jpg"), "Foo");
            assertEquals(item.getImages().get("bar.jpg"), "Bar");
            assertEquals(item.getImages().get("baz.jpg"), "Baz");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
