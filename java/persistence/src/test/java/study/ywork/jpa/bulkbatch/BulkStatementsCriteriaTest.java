package study.ywork.jpa.bulkbatch;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.bulkbatch.BankAccount;
import study.ywork.jpa.model.bulkbatch.Bid;
import study.ywork.jpa.model.bulkbatch.CreditCard;
import study.ywork.jpa.model.bulkbatch.CreditCard_;
import study.ywork.jpa.model.bulkbatch.Item;
import study.ywork.jpa.model.bulkbatch.Item_;
import study.ywork.jpa.model.bulkbatch.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BulkStatementsCriteriaTest extends JpaManager {
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
            CriteriaBuilder criteriaBuilder =
                    jpa.getEntityManagerFactory().getCriteriaBuilder();

            tx.begin();
            EntityManager em = jpa.createEntityManager();

            long ITEM_ID = testData.items.getFirstId();
            long USER_ID = testData.users.getFirstId();

            Item someItem = em.find(Item.class, ITEM_ID);
            User tester = em.find(User.class, USER_ID);
            int originalVersion = someItem.getVersion();

            assertEquals(someItem.getSeller(), tester);
            assertFalse(someItem.isActive());

            CriteriaUpdate<Item> update =
                    criteriaBuilder.createCriteriaUpdate(Item.class);
            Root<Item> i = update.from(Item.class);
            update.set(i.get(Item_.active), true);
            update.where(
                    criteriaBuilder.equal(i.get(Item_.seller), tester)
            );

            int updatedEntities = em.createQuery(update).executeUpdate();

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
    public void bulkUpdateVersioned() throws Exception {
        BulkBatchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            CriteriaBuilder criteriaBuilder =
                    jpa.getEntityManagerFactory().getCriteriaBuilder();

            tx.begin();
            EntityManager em = jpa.createEntityManager();

            long ITEM_ID = testData.items.getFirstId();

            Item someItem = em.find(Item.class, ITEM_ID);

            int originalVersion = someItem.getVersion();

            CriteriaUpdate<Item> update =
                    criteriaBuilder.createCriteriaUpdate(Item.class);

            Root<Item> i = update.from(Item.class);

            update.set(i.get(Item_.active), true);

            update.set(
                    i.get(Item_.version),
                    criteriaBuilder.sum(i.get(Item_.version), 1)
            );

            int updatedEntities = em.createQuery(update).executeUpdate();

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
            CriteriaBuilder criteriaBuilder =
                    jpa.getEntityManagerFactory().getCriteriaBuilder();

            tx.begin();
            EntityManager em = jpa.createEntityManager();

            List<CreditCard> creditCards =
                    em.createQuery("select c from CreditCard c").getResultList();

            CriteriaDelete<CreditCard> delete =
                    criteriaBuilder.createCriteriaDelete(CreditCard.class);

            Root<CreditCard> c = delete.from(CreditCard.class);

            delete.where(
                    criteriaBuilder.like(
                            c.get(CreditCard_.owner),
                            "J%"
                    )
            );

            em.createQuery(delete).executeUpdate();

            for (CreditCard creditCard : creditCards) {
                em.refresh(creditCard);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
