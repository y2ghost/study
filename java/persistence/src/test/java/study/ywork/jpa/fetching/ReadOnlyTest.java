package study.ywork.jpa.fetching;

import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.fetching.readonly.Bid;
import study.ywork.jpa.model.fetching.readonly.Item;
import study.ywork.jpa.model.fetching.readonly.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertNotEquals;

public class ReadOnlyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("fetchingReadOnly");
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

        tx.commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    public void immutableEntity() throws Exception {
        FetchTestData testData = storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Long itemId = testData.items.getFirstId();

            Item item = em.find(Item.class, itemId);
            for (Bid bid : item.getBids()) {
                bid.setAmount(new BigDecimal("99.99"));
            }
            em.flush();
            em.clear();

            item = em.find(Item.class, itemId);
            for (Bid bid : item.getBids()) {
                assertNotEquals(bid.getAmount().toString(), "99.99");
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void selectiveReadOnly() throws Exception {
        FetchTestData testData = storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Long itemId = testData.items.getFirstId();

            {
                em.unwrap(Session.class).setDefaultReadOnly(true);
                Item item = em.find(Item.class, itemId);
                item.setName("New Name");
                em.flush();
            }
            {
                em.clear();
                Item item = em.find(Item.class, itemId);
                assertNotEquals(item.getName(), "New Name");
            }
            {
                Item item = em.find(Item.class, itemId);
                em.unwrap(Session.class).setReadOnly(item, true);
                item.setName("New Name");
                em.flush();
            }
            {
                em.clear();
                Item item = em.find(Item.class, itemId);
                assertNotEquals(item.getName(), "New Name");
            }
            {
                org.hibernate.query.Query query = em.unwrap(Session.class)
                        .createQuery("select i from Item i");

                query.setReadOnly(true).list();
                List<Item> result = query.list();

                for (Item item : result)
                    item.setName("New Name");

                em.flush();
            }
            {
                List<Item> items = em.createQuery("select i from Item i")
                        .setHint(
                                org.hibernate.annotations.QueryHints.READ_ONLY,
                                true
                        ).getResultList();

                for (Item item : items)
                    item.setName("New Name");
                em.flush();
            }
            {
                em.clear();
                Item item = em.find(Item.class, itemId);
                assertNotEquals(item.getName(), "New Name");
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
