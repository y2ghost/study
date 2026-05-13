package study.ywork.jpa.bulkbatch;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.bulkbatch.BankAccount;
import study.ywork.jpa.model.bulkbatch.Bid;
import study.ywork.jpa.model.bulkbatch.CreditCard;
import study.ywork.jpa.model.bulkbatch.Item;
import study.ywork.jpa.model.bulkbatch.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BulkStatementsSQLTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("bulkBatch");
    }

    public static class BulkBatchTestData {
        public TestData items;
        public TestData users;
    }

    public BulkBatchTestData storeTestData() throws Exception {
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

        item = new Item("Item_Three", CalendarUtil.AFTER_TOMORROW.getTime(), dev);
        em.persist(item);
        itemIds[2] = item.getId();
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, tester, new BigDecimal(3 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        em.persist(new BankAccount(dev, "Jane Roe", "445566", "One Percent Bank Inc.", "999"));
        em.persist(new CreditCard(tester, "John Doe", "1234123412341234", "06", "2015"));

        tx.commit();
        em.close();

        BulkBatchTestData testData = new BulkBatchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test(groups = {"H2", "MARIADB", "POSTGRESQL"})
    public void bulkUpdate() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            long ITEM_ID = testData.items.getFirstId();
            long USER_ID = testData.users.getFirstId();

            Item someItem = em.find(Item.class, ITEM_ID);
            User tester = em.find(User.class, USER_ID);
            int originalVersion = someItem.getVersion();

            assertEquals(someItem.getSeller(), tester);
            assertFalse(someItem.isActive());

            Query query = em.createNativeQuery(
                    "update ITEM set ACTIVE = true where SELLER_ID = :sellerId"
            ).setParameter("sellerId", tester.getId());

            int updatedEntities = query.executeUpdate();
            // All second-level cache regions have been cleared!

            assertEquals(updatedEntities, 2); // Updated rows not entity instances!

            assertFalse(someItem.isActive());
            em.refresh(someItem); // Update the instance in persistence context
            assertTrue(someItem.isActive());

            assertEquals(someItem.getVersion(), originalVersion); // Version wasn't incremented!

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2", "MARIADB", "POSTGRESQL"})
    public void bulkUpdateHibernate() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            long ITEM_ID = testData.items.getFirstId();
            long USER_ID = testData.users.getFirstId();

            Item someItem = em.find(Item.class, ITEM_ID);
            User tester = em.find(User.class, USER_ID);
            int originalVersion = someItem.getVersion();

            assertEquals(someItem.getSeller(), tester);
            assertFalse(someItem.isActive());

            org.hibernate.SQLQuery query =
                    em.unwrap(org.hibernate.Session.class).createSQLQuery(
                            "update ITEM set ACTIVE = true where SELLER_ID = :sellerId"
                    );
            query.setParameter("sellerId", tester.getId());

            query.addSynchronizedEntityClass(Item.class);

            int updatedEntities = query.executeUpdate();

            assertEquals(updatedEntities, 2);

            assertFalse(someItem.isActive());
            em.refresh(someItem);
            assertTrue(someItem.isActive());

            assertEquals(someItem.getVersion(), originalVersion);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
