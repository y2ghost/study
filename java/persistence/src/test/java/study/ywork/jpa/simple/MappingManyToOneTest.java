package study.ywork.jpa.simple;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.simple.Bid;
import study.ywork.jpa.model.simple.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class MappingManyToOneTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simple");
    }

    @Test
    public void storeAndLoadBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item anItem = new Item();
            anItem.setName("Example Item");
            Bid firstBid = new Bid(new BigDecimal("123.00"), anItem);
            Bid secondBid = new Bid(new BigDecimal("456.00"), anItem);
            em.persist(anItem);
            em.persist(firstBid);
            em.persist(secondBid);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Long bidId = firstBid.getId();
            Bid someBid = em.find(Bid.class, bidId);

            assertEquals(someBid.getItem().getId(), anItem.getId());

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
