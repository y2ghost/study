package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.setofembeddablesorderby.Image;
import study.ywork.jpa.model.collections.setofembeddablesorderby.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class SetOfEmbeddableOrderByTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("setOfEmbeddableOrderBy");
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
                    "Bar", "bar.jpg", 800, 600
            ));
            someItem.getImages().add(new Image(
                    "Baz", "baz.jpg", 1024, 768
            ));
            someItem.getImages().add(new Image(
                    "Baz", "baz.jpg", 1024, 768
            ));
            assertEquals(someItem.getImages().size(), 3);

            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            assertEquals(item.getImages().size(), 3);

            Iterator<Image> it = item.getImages().iterator();
            assertEquals(it.next().getFilename(), "bar.jpg");
            assertEquals(it.next().getFilename(), "baz.jpg");
            assertEquals(it.next().getFilename(), "foo.jpg");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
