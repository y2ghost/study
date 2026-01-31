package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.mapofstringsorderby.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class MapOfStringsOrderByTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mapOfStringsOrderBy");
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

            Iterator<Map.Entry<String, String>> it = item.getImages().entrySet().iterator();
            Map.Entry<String, String> entry;
            entry = it.next();
            assertEquals(entry.getKey(), "foo.jpg");
            assertEquals(entry.getValue(), "Foo");
            entry = it.next();
            assertEquals(entry.getKey(), "baz.jpg");
            assertEquals(entry.getValue(), "Baz");
            entry = it.next();
            assertEquals(entry.getKey(), "bar.jpg");
            assertEquals(entry.getValue(), "Bar");

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
