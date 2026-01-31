package study.ywork.jpa.fetching;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxyHelper;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.fetching.proxy.Bid;
import study.ywork.jpa.model.fetching.proxy.Category;
import study.ywork.jpa.model.fetching.proxy.Item;
import study.ywork.jpa.model.fetching.proxy.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public class LazyProxyCollectionsTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("fetchingProxy");
    }

    public FetchTestData storeTestData() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        Long[] categoryIds = new Long[3];
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

        Category category = new Category("Category One");
        em.persist(category);
        categoryIds[0] = category.getId();

        Item item = new Item("Item One", CalendarUtil.TOMORROW.getTime(), tester);
        em.persist(item);
        itemIds[0] = item.getId();
        category.getItems().add(item);
        item.getCategories().add(category);
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, manager, new BigDecimal(9 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        category = new Category("Category Two");
        em.persist(category);
        categoryIds[1] = category.getId();

        item = new Item("Item Two", CalendarUtil.TOMORROW.getTime(), tester);
        em.persist(item);
        itemIds[1] = item.getId();
        category.getItems().add(item);
        item.getCategories().add(category);
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, dev, new BigDecimal(2 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Three", CalendarUtil.AFTER_TOMORROW.getTime(), dev);
        em.persist(item);
        itemIds[2] = item.getId();
        category.getItems().add(item);
        item.getCategories().add(category);

        category = new Category("Category Three");
        em.persist(category);
        categoryIds[2] = category.getId();

        tx.commit();
        em.close();

        FetchTestData testData = new FetchTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    public void lazyEntityProxies() throws Exception {
        FetchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Long itemId = testData.items.getFirstId();
            Long userId = testData.users.getFirstId();

            {
                Item item = em.getReference(Item.class, itemId);

                assertEquals(item.getId(), itemId);
                assertNotEquals(item.getClass(), Item.class);

                assertEquals(
                        HibernateProxyHelper.getClassWithoutInitializingProxy(item),
                        Item.class
                );

                PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
                assertFalse(persistenceUtil.isLoaded(item));
                assertFalse(persistenceUtil.isLoaded(item, "seller"));

                assertFalse(Hibernate.isInitialized(item));

                Hibernate.initialize(item);
                assertFalse(Hibernate.isInitialized(item.getSeller()));

                Hibernate.initialize(item.getSeller());
            }
            em.clear();
            {
                Item item = em.find(Item.class, itemId);
                em.detach(item);
                em.detach(item.getSeller());
                PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
                assertTrue(persistenceUtil.isLoaded(item));
                assertFalse(persistenceUtil.isLoaded(item, "seller"));
                assertEquals(item.getSeller().getId(), userId);
            }
            em.clear();
            {
                Item item = em.getReference(Item.class, itemId);
                User user = em.getReference(User.class, userId);
                Bid newBid = new Bid(new BigDecimal("99.00"));
                newBid.setItem(item);
                newBid.setBidder(user);
                em.persist(newBid);
                em.flush();
                em.clear();
                assertEquals(em.find(Bid.class, newBid.getId()).getAmount().compareTo(new BigDecimal("99")), 0);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void lazyCollections() throws Exception {
        FetchTestData testData = storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            long itemId = testData.items.getFirstId();

            {
                Item item = em.find(Item.class, itemId);
                Set<Bid> bids = item.getBids();
                PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
                assertFalse(persistenceUtil.isLoaded(item, "bids"));
                assertTrue(Set.class.isAssignableFrom(bids.getClass()));
                assertNotEquals(bids.getClass(), HashSet.class);
                assertEquals(bids.getClass(), org.hibernate.collection.internal.PersistentSet.class);
                Bid firstBid = bids.iterator().next();
            }
            em.clear();
            {
                Item item = em.find(Item.class, itemId);
                assertEquals(item.getBids().size(), 3);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
