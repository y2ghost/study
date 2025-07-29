package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.bagofembeddables.Image;
import study.ywork.jpa.model.collections.bagofembeddables.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class BagOfEmbeddableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("bagOfEmbeddable");
    }

    @Test
    public void storeLoadCollection() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item someItem = new Item();

            someItem.getImages().add(new Image(
                    "Foo", "foo.jpg", 640, 480
            ));
            someItem.getImages().add(new Image(
                    null, "bar.jpg", 800, 600 // Columns can be NULL now!
            ));
            someItem.getImages().add(new Image(
                    "Baz", "baz.jpg", 1024, 768
            ));
            someItem.getImages().add(new Image(
                    "Baz", "baz.jpg", 1024, 768
            ));

            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 4);
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
