package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetomany.orphanremoval.Bid;
import study.ywork.jpa.model.associations.onetomany.orphanremoval.Item;
import study.ywork.jpa.model.associations.onetomany.orphanremoval.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class OneToManyOrphanRemovalTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToManyOrphanRemoval");
    }

    @Test
    public void storeAndLoadItemBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User tester = new User();
            em.persist(tester);

            Item anItem = new Item("Some Item");
            Bid bidA = new Bid(new BigDecimal("123.00"), anItem);
            bidA.setBidder(tester);
            anItem.getBids().add(bidA);

            Bid bidB = new Bid(new BigDecimal("456.00"), anItem);
            anItem.getBids().add(bidB);
            bidB.setBidder(tester);

            em.persist(anItem);

            tx.commit();
            em.close();
            Long itemId = anItem.getId();
            Long userId = tester.getId();

            tx.begin();
            em = jpa.createEntityManager();

            User user = em.find(User.class, userId);
            assertEquals(user.getBids().size(), 2);

            Item item = em.find(Item.class, itemId);
            Bid firstBid = item.getBids().iterator().next();
            item.getBids().remove(firstBid);

            assertEquals(user.getBids().size(), 2);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            item = em.find(Item.class, itemId);
            assertEquals(item.getBids().size(), 1);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}