package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.mapofembeddables.Filename;
import study.ywork.jpa.model.collections.mapofembeddables.Image;
import study.ywork.jpa.model.collections.mapofembeddables.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class MapOfEmbeddableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mapOfEmbeddable");
    }

    @Test
    public void storeLoadCollection() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();

            someItem.getImages().put(
                    new Filename("foo", "jpg"),
                    new Image("Foo", 640, 480));
            someItem.getImages().put(
                    new Filename("bar", "jpg"),
                    new Image(null, 800, 600));
            someItem.getImages().put(
                    new Filename("baz", "jpg"),
                    new Image("Baz", 1024, 768));
            someItem.getImages().put(
                    new Filename("baz", "jpg"),
                    new Image("Baz", 1024, 768)); // Duplicate key filtered!

            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 3);
            assertEquals(item.getImages().get(new Filename("foo", "jpg")).getTitle(), "Foo");
            assertEquals(item.getImages().get(new Filename("bar", "jpg")).getTitle(), null);
            assertEquals(item.getImages().get(new Filename("baz", "jpg")), new Image("Baz", 1024, 768));

            item.getImages().remove(new Filename("foo", "jpg"));
            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 2);
            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}
