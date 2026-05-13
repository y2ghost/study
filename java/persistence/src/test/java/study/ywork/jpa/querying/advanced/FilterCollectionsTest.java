package study.ywork.jpa.querying.advanced;

import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FilterCollectionsTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        Long ITEM_ID = testData.items.getFirstId();
        Long USER_ID = testData.users.getLastId();


        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Session session = em.unwrap(Session.class);

            {
                Item item = em.find(Item.class, ITEM_ID);
                User user = em.find(User.class, USER_ID);

                org.hibernate.Query query = session.createFilter(
                        item.getBids(),
                        "where this.bidder = :bidder order by this.amount desc"
                );

                query.setParameter("bidder", user);
                List<Bid> bids = query.list();

                assertEquals(bids.size(), 3);
                assertEquals(bids.get(0).getBidder(), user);
                assertEquals(bids.get(0).getAmount().compareTo(new BigDecimal("101")), 0);
                assertEquals(bids.get(1).getAmount().compareTo(new BigDecimal("100")), 0);
                assertEquals(bids.get(2).getAmount().compareTo(new BigDecimal("99")), 0);
            }
            em.clear();

            {
                Item item = em.find(Item.class, ITEM_ID);

                org.hibernate.Query query = session.createFilter(
                        item.getBids(),
                        ""
                );

                query.setFirstResult(0);
                query.setMaxResults(2);
                List<Bid> bids = query.list();

                assertEquals(bids.size(), 2);
            }
            em.clear();

            {
                Item item = em.find(Item.class, ITEM_ID);

                org.hibernate.Query query = session.createFilter(
                        item.getBids(),
                        "from Item i where i.seller = this.bidder"
                );

                List<Item> items = query.list();

                assertEquals(items.size(), 0);
            }
            em.clear();

            {
                Item item = em.find(Item.class, ITEM_ID);

                org.hibernate.Query query = session.createFilter(
                        item.getBids(),
                        "select distinct this.bidder.username order by this.bidder.username asc"
                );

                List<String> bidders = query.list();

                assertEquals(bidders.size(), 1);
                assertEquals(bidders.get(0), "manager");
            }
            em.clear();

            {
                Item item = em.find(Item.class, ITEM_ID);

                org.hibernate.Query query = session.createFilter(
                        item.getBids(),
                        "where this.amount >= :param"
                );

                query.setParameter("param", new BigDecimal(100));
                List<Bid> bids = query.list();

                assertEquals(bids.size(), 2);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

}
