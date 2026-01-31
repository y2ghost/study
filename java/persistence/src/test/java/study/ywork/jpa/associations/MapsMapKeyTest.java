package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.maps.mapkey.Bid;
import study.ywork.jpa.model.associations.maps.mapkey.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class MapsMapKeyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mapsMapKey");
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
            em.persist(someBid);
            someItem.getBids().put(someBid.getId(), someBid); // Optional...

            Bid secondBid = new Bid(new BigDecimal("456.00"), someItem);
            em.persist(secondBid);
            someItem.getBids().put(secondBid.getId(), secondBid); // Optional...

            tx.commit();
            em.close();

            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertEquals(item.getBids().size(), 2);

            for (Map.Entry<Long, Bid> entry : item.getBids().entrySet()) {
                assertEquals(entry.getKey(), entry.getValue().getId());
            }

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}