package study.ywork.jpa.concurrency;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.concurrency.version.Bid;
import study.ywork.jpa.model.concurrency.version.Category;
import study.ywork.jpa.model.concurrency.version.InvalidBidException;
import study.ywork.jpa.model.concurrency.version.Item;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executors;

import static org.testng.Assert.assertEquals;

public class VersioningTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("concurrencyVersioning");
    }

    @Test(expectedExceptions = OptimisticLockException.class)
    public void firstCommitWins() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            final Long ITEM_ID = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, ITEM_ID);
            assertEquals(item.getVersion(), 0);
            item.setName("New Name");

            Executors.newSingleThreadExecutor().submit(() -> {
                UserTransaction tx1 = tm.getUserTransaction();
                try {
                    tx1.begin();
                    EntityManager em1 = jpa.createEntityManager();
                    Item item1 = em1.find(Item.class, ITEM_ID);
                    assertEquals(item1.getVersion(), 0);
                    item1.setName("Other Name");
                    tx1.commit();
                    em1.close();
                } catch (Exception ex) {
                    tm.rollback();
                    throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                }
                return null;
            }).get();
            em.flush();
        } catch (Exception ex) {
            throw unwrapCauseOfType(ex, OptimisticLockException.class);
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = OptimisticEntityLockException.class)
    public void manualVersionChecking() throws Throwable {
        final ConcurrencyTestData testData = storeCategoriesAndItems();
        Long[] categories = testData.categories.identifiers;
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            BigDecimal totalPrice = new BigDecimal(0);
            for (Long categoryId : categories) {
                List<Item> items =
                        em.createQuery("select i from Item i where i.category.id = :catId")
                                .setLockMode(LockModeType.OPTIMISTIC)
                                .setParameter("catId", categoryId)
                                .getResultList();

                for (Item item : items) {
                    totalPrice = totalPrice.add(item.getBuyNowPrice());
                }

                if (categoryId.equals(testData.categories.getFirstId())) {
                    Executors.newSingleThreadExecutor().submit(() -> {
                        UserTransaction tx1 = tm.getUserTransaction();
                        try {
                            tx1.begin();
                            EntityManager em1 = jpa.createEntityManager();

                            List<Item> items1 =
                                    em1.createQuery("select i from Item i where i.category.id = :catId")
                                            .setParameter("catId", testData.categories.getFirstId())
                                            .getResultList();

                            Category lastCategory = em1.getReference(
                                    Category.class, testData.categories.getLastId()
                            );

                            items1.iterator().next().setCategory(lastCategory);
                            tx1.commit();
                            em1.close();
                        } catch (Exception ex) {
                            tm.rollback();
                            throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                        }
                        return null;
                    }).get();
                }
            }

            tx.commit();
            em.close();

            assertEquals(totalPrice.toString(), "108.00");
        } catch (Exception ex) {
            throw unwrapCauseOfType(ex, OptimisticEntityLockException.class);
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.StaleObjectStateException.class)
    public void forceIncrement() throws Throwable {
        final TestData testData = storeItemAndBids();
        Long itemId = testData.getFirstId();
        UserTransaction tx = tm.getUserTransaction();

        try {

            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item item = em.find(
                    Item.class,
                    itemId,
                    LockModeType.OPTIMISTIC_FORCE_INCREMENT
            );

            Bid highestBid = queryHighestBid(em, item);
            Executors.newSingleThreadExecutor().submit(() -> {
                UserTransaction tx1 = tm.getUserTransaction();
                try {
                    tx1.begin();
                    EntityManager em1 = jpa.createEntityManager();

                    Item item1 = em1.find(
                            Item.class,
                            testData.getFirstId(),
                            LockModeType.OPTIMISTIC_FORCE_INCREMENT
                    );
                    Bid highestBid1 = queryHighestBid(em1, item1);
                    try {
                        Bid newBid = new Bid(
                                new BigDecimal("44.44"),
                                item1,
                                highestBid1
                        );
                        em1.persist(newBid);
                    } catch (InvalidBidException ex) {
                        // NOOP
                    }

                    tx1.commit();
                    em1.close();
                } catch (Exception ex) {
                    tm.rollback();
                    throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                }
                return null;
            }).get();

            try {
                Bid newBid = new Bid(
                        new BigDecimal("44.44"),
                        item,
                        highestBid
                );
                em.persist(newBid);
            } catch (InvalidBidException ex) {
                // NOOP
            }

            tx.commit();
            em.close();
        } catch (Exception ex) {
            throw unwrapCauseOfType(ex, org.hibernate.StaleObjectStateException.class);
        } finally {
            tm.rollback();
        }
    }

    class ConcurrencyTestData {
        TestData categories;
        TestData items;
    }

    public ConcurrencyTestData storeCategoriesAndItems() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();
        ConcurrencyTestData testData = new ConcurrencyTestData();
        testData.categories = new TestData(new Long[3]);
        testData.items = new TestData(new Long[5]);
        for (int i = 1; i <= testData.categories.identifiers.length; i++) {
            Category category = new Category();
            category.setName("Category: " + i);
            em.persist(category);
            testData.categories.identifiers[i - 1] = category.getId();
            for (int j = 1; j <= testData.categories.identifiers.length; j++) {
                Item item = new Item("Item " + j);
                item.setCategory(category);
                item.setBuyNowPrice(new BigDecimal(10 + j));
                em.persist(item);
                testData.items.identifiers[(i - 1) + (j - 1)] = item.getId();
            }
        }
        tx.commit();
        em.close();
        return testData;
    }

    public TestData storeItemAndBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();
        Long[] ids = new Long[1];
        Item item = new Item("Some Item");
        em.persist(item);
        ids[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(new BigDecimal(10 + i), item);
            em.persist(bid);
        }
        tx.commit();
        em.close();
        return new TestData(ids);
    }

    protected Bid queryHighestBid(EntityManager em, Item item) {
        try {
            return (Bid) em.createQuery(
                            "select b from Bid b" +
                                    " where b.item = :itm" +
                                    " order by b.amount desc"
                    )
                    .setParameter("itm", item)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
