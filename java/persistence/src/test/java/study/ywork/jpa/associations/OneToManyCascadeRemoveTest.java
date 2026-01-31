package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetomany.cascaderemove.Bid;
import study.ywork.jpa.model.associations.onetomany.cascaderemove.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Collection;

import static org.testng.Assert.assertEquals;

public class OneToManyCascadeRemoveTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToManyCascadeRemove");
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

            Bid secondBid = new Bid(new BigDecimal("456.00"), someItem);
            someItem.getBids().add(secondBid);

            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Collection<Bid> bids =
                    em.createQuery("select b from Bid b where b.item.id = :itemId")
                            .setParameter("itemId", itemId)
                            .getResultList();
            assertEquals(bids.size(), 2);
            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            em.remove(item);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            bids = em.createQuery("select b from Bid b where b.item.id = :itemId")
                    .setParameter("itemId", itemId)
                    .getResultList();
            assertEquals(bids.size(), 0);
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}