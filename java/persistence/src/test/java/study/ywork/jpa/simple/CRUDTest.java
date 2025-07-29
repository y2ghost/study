package study.ywork.jpa.simple;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.simple.Item;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class CRUDTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simple");
    }

    @Test
    public void storeAndQueryItems() throws Exception {
        storeAndQueryItems("findItems");
    }

    public void storeAndQueryItems(String queryName) throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item itemOne = new Item();
            itemOne.setName("Item One");
            itemOne.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));
            em.persist(itemOne);

            Item itemTwo = new Item();
            itemTwo.setName("Item Two");
            itemTwo.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));

            em.persist(itemTwo);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Query q = em.createNamedQuery(queryName);
            List<Item> items = q.getResultList();

            assertEquals(items.size(), 2);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
