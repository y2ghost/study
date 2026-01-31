package study.ywork.jpa.fetching;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.fetching.nplusoneselects.Bid;
import study.ywork.jpa.model.fetching.nplusoneselects.Item;
import study.ywork.jpa.model.fetching.nplusoneselects.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class EagerQueryTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("fetchingNPlusOneSelects");
    }

    public FetchTestData storeTestData() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];

        User tester = new User("tester");
        em.persist(tester);
        userIds[0] = tester.getId();

        User dev = new User("dev");
        em.persist(dev);
        userIds[1] = dev.getId();

        User manager = new User("manager");
        em.persist(manager);
        userIds[2] = manager.getId();

        Item item = new Item("Item One", CalendarUtil.TOMORROW.getTime(), tester);
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, manager, new BigDecimal(9 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Two", CalendarUtil.TOMORROW.getTime(), tester);
        em.persist(item);
        itemIds[1] = item.getId();
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, dev, new BigDecimal(2 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Three", CalendarUtil.AFTER_TOMORROW.getTime(), dev);
        em.persist(item);
        itemIds[2] = item.getId();
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, tester, new BigDecimal(3 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        tx.commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    public void fetchUsers() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            {
                EntityManager em = jpa.createEntityManager();
                List<Item> items =
                        em.createQuery("select i from Item i join fetch i.seller")
                                .getResultList();
                em.close();

                for (Item item : items) {
                    assertNotNull(item.getSeller().getUsername());
                }
            }
            {
                EntityManager em = jpa.createEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery criteria = cb.createQuery();

                Root<Item> i = criteria.from(Item.class);
                i.fetch("seller");
                criteria.select(i);

                List<Item> items = em.createQuery(criteria).getResultList();
                em.close();

                for (Item item : items) {
                    assertNotNull(item.getSeller().getUsername());
                }
            }
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void fetchBids() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();

            {
                EntityManager em = jpa.createEntityManager();
                List<Item> items =
                        em.createQuery("select i from Item i left join fetch i.bids")
                                .getResultList();
                em.close();

                for (Item item : items) {
                    assertTrue(item.getBids().size() > 0);
                }
            }
            {
                EntityManager em = jpa.createEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery criteria = cb.createQuery();

                Root<Item> i = criteria.from(Item.class);
                i.fetch("bids", JoinType.LEFT);
                criteria.select(i);

                List<Item> items = em.createQuery(criteria).getResultList();

                em.close();

                for (Item item : items) {
                    assertTrue(item.getBids().size() > 0);
                }
            }
        } finally {
            tm.rollback();
        }
    }
}
