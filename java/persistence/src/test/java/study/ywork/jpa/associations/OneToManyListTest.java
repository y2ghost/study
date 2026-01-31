package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetomany.list.Bid;
import study.ywork.jpa.model.associations.onetomany.list.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class OneToManyListTest extends JpaManager {

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToManyList");
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
            em.persist(someBid);

            Bid secondBid = new Bid(new BigDecimal("456.00"), someItem);
            someItem.getBids().add(secondBid);
            em.persist(secondBid);

            assertEquals(someItem.getBids().size(), 2);

            tx.commit();
            em.close();

            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            List<Bid> bids = item.getBids();
            assertEquals(bids.size(), 2);
            assertEquals(bids.get(0).getAmount().compareTo(new BigDecimal("123")), 0);
            assertEquals(bids.get(1).getAmount().compareTo(new BigDecimal("456")), 0);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}