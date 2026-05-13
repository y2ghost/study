package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetomany.bag.Bid;
import study.ywork.jpa.model.associations.onetomany.bag.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class OneToManyBagTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToManyBag");
    }

    @Test
    public void storeAndLoadItemBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item someItem = new Item("Some Item");
            em.persist(someItem);

            Bid someBid = new Bid(new BigDecimal("123.00"), someItem);
            someItem.getBids().add(someBid);
            someItem.getBids().add(someBid);
            em.persist(someBid);

            assertEquals(someItem.getBids().size(), 2);

            tx.commit();
            em.close();

            Long someItemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            {
                Item item = em.find(Item.class, someItemId);
                assertEquals(item.getBids().size(), 1);
            }
            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            {
                Item item = em.find(Item.class, someItemId);

                Bid bid = new Bid(new BigDecimal("456.00"), item);
                item.getBids().add(bid);
                em.persist(bid);
            }
            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}