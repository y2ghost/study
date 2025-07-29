package study.ywork.jpa.bulkbatch;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.bulkbatch.BankAccount;
import study.ywork.jpa.model.bulkbatch.Bid;
import study.ywork.jpa.model.bulkbatch.CreditCard;
import study.ywork.jpa.model.bulkbatch.Item;
import study.ywork.jpa.model.bulkbatch.StolenCreditCard;
import study.ywork.jpa.model.bulkbatch.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BulkStatementsJPQLTest extends JpaManager {
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

    @Test
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

            Query query = em.createQuery(
                    "update Item i set i.active = true where i.seller = :s"
            ).setParameter("s", tester);

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

    @Test
    public void bulkUpdateInheritance() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Query query = em.createQuery(
                    "update CreditCard c set c.stolenOn = :now where c.owner like 'J%'"
            ).setParameter("now", new Date());

            int updatedEntities = query.executeUpdate();
            assertEquals(updatedEntities, 1);

            List<CreditCard> creditCards =
                    em.createQuery("select c from CreditCard c").getResultList();

            assertEquals(creditCards.size(), 1);
            for (CreditCard creditCard : creditCards) {
                assertTrue(creditCard.getStolenOn().getTime() > 0);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void bulkUpdateVersioned() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            long ITEM_ID = testData.items.getFirstId();

            Item someItem = em.find(Item.class, ITEM_ID);

            int originalVersion = someItem.getVersion();

            int updatedEntities =
                    em.createQuery("update versioned Item i set i.active = true")
                            .executeUpdate();

            assertEquals(updatedEntities, 3);

            em.refresh(someItem);
            assertTrue(someItem.getVersion() > originalVersion);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void bulkDelete() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            List<CreditCard> creditCards =
                    em.createQuery("select c from CreditCard c").getResultList();

            em.createQuery("delete CreditCard c where c.owner like 'J%'")
                    .executeUpdate();

            for (CreditCard creditCard : creditCards) {
                em.refresh(creditCard);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void bulkInsert() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            int createdRecords =
                    em.createQuery(
                            "insert into" +
                                    " StolenCreditCard(id, owner, cardNumber, expMonth, expYear, userId, username)" +
                                    " select c.id, c.owner, c.cardNumber, c.expMonth, c.expYear, u.id, u.username" +
                                    " from CreditCard c join c.user u where c.owner like 'J%'"
                    ).executeUpdate();

            assertEquals(createdRecords, 1);

            List<StolenCreditCard> stolenCreditCards =
                    em.createQuery("select sc from StolenCreditCard sc").getResultList();
            assertEquals(stolenCreditCards.size(), 1);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
