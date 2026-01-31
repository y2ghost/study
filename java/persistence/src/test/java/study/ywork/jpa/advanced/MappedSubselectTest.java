package study.ywork.jpa.advanced;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Bid;
import study.ywork.jpa.model.advanced.Item;
import study.ywork.jpa.model.advanced.ItemBidSummary;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class MappedSubselectTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }

    @Test
    public void loadSubselectEntity() throws Exception {
        long itemId = storeItemAndBids();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                ItemBidSummary itemBidSummary = em.find(ItemBidSummary.class, itemId);
                assertEquals(itemBidSummary.getName(), "AUCTION: Some item");
            }
            em.clear();

            {
                Item item = em.find(Item.class, itemId);
                item.setName("New name");
                Query query = em.createQuery(
                        "select ibs from ItemBidSummary ibs where ibs.itemId = :id"
                );
                ItemBidSummary itemBidSummary =
                        (ItemBidSummary) query.setParameter("id", itemId).getSingleResult();
                assertEquals(itemBidSummary.getName(), "AUCTION: New name");
            }

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
