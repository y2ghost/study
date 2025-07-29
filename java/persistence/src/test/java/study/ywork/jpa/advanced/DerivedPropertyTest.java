package study.ywork.jpa.advanced;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Bid;
import study.ywork.jpa.model.advanced.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class DerivedPropertyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }

    @Test
    public void storeLoadFormula() throws Exception {
        long itemId = storeItemAndBids();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertEquals(item.getShortDescription(), "This is some...");

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void storeLoadFormulaSubselect() throws Exception {
        long itemId = storeItemAndBids();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertEquals(item.getAverageBidAmount().compareTo(new BigDecimal("12")), 0);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    public Long storeItemAndBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();
        Item item = new Item();
        item.setName("Some item");
        item.setDescription("This is some description.");
        em.persist(item);

        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid();
            bid.setAmount(new BigDecimal(10 + i));
            bid.setItem(item);
            em.persist(bid);
        }
        tx.commit();
        em.close();
        return item.getId();
    }
}
