package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.mapofstringsembeddables.Image;
import study.ywork.jpa.model.collections.mapofstringsembeddables.Item;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class MapOfStringsEmbeddableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mapOfStringsEmbeddable");
    }

    @Test
    public void storeLoadCollection() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();

            someItem.getImages().put("foo.jpg",
                    new Image("Foo", 640, 480));
            someItem.getImages().put("bar.jpg",
                    new Image(null, 800, 600));
            someItem.getImages().put("baz.jpg",
                    new Image("Baz", 1024, 768));
            someItem.getImages().put("baz.jpg",
                    new Image("Baz", 1024, 768));

            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 3);
            assertEquals(item.getImages().get("foo.jpg").getTitle(), "Foo");
            assertNull(item.getImages().get("bar.jpg").getTitle());
            assertEquals(item.getImages().get("baz.jpg"), new Image("Baz", 1024, 768));
            tx.commit();
            em.close();

            {
                tx.begin();
                em = jpa.createEntityManager();
                Query q = em.createQuery("select value(img) from Item i join i.images img where key(img) like '%.jpg'");
                List<Image> result = q.getResultList();
                assertEquals(result.size(), 3);
                assertTrue(result.get(0) instanceof Image);
                tx.commit();
                em.close();
            }
            {
                tx.begin();
                em = jpa.createEntityManager();
                Query q = em.createQuery("select entry(img) from Item i join i.images img where key(img) like '%.jpg'");
                List<Map.Entry<String, Image>> result = q.getResultList();
                assertEquals(result.size(), 3);
                assertTrue(result.get(0) instanceof Map.Entry);
                assertTrue(result.get(0).getKey().endsWith(".jpg"));
                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }
}
