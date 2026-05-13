package study.ywork.jpa.cache;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.NaturalIdCacheStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.cache.Bid;
import study.ywork.jpa.model.cache.Item;
import study.ywork.jpa.model.cache.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.management.ObjectName;
import javax.persistence.Cache;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class SecondLevelTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("cache");
    }

    public static class CacheTestData {
        public TestData items;
        public TestData users;
    }

    public CacheTestData storeTestData() throws Exception {
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

        tx.commit();
        em.close();

        tx.begin();
        em = jpa.createEntityManager();
        em.createQuery("select u from User u").getResultList();

        for (Long itemId : itemIds) {
            em.find(Item.class, itemId).getBids().size();
        }
        tx.commit();
        em.close();

        jpa.getEntityManagerFactory()
                .unwrap(SessionFactory.class)
                .getStatistics().clear();

        CacheTestData testData = new CacheTestData();
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    public void cacheBehavior() throws Exception {
        CacheTestData testData = storeTestData();
        Long USER_ID = testData.users.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                Statistics stats =
                        jpa.getEntityManagerFactory()
                                .unwrap(SessionFactory.class)
                                .getStatistics();

                SecondLevelCacheStatistics itemCacheStats =
                        stats.getSecondLevelCacheStatistics(Item.class.getName());
                assertEquals(itemCacheStats.getElementCountInMemory(), 3);
                assertEquals(itemCacheStats.getHitCount(), 0);
                Item item = em.find(Item.class, ITEM_ID);
                assertEquals(itemCacheStats.getHitCount(), 1);
                SecondLevelCacheStatistics userCacheStats =
                        stats.getSecondLevelCacheStatistics(User.class.getName());
                assertEquals(userCacheStats.getElementCountInMemory(), 3);
                assertEquals(userCacheStats.getHitCount(), 0);

                User seller = item.getSeller();
                assertEquals(seller.getUsername(), "tester");

                assertEquals(userCacheStats.getHitCount(), 1);

                SecondLevelCacheStatistics bidsCacheStats =
                        stats.getSecondLevelCacheStatistics(Item.class.getName() + ".bids");
                assertEquals(bidsCacheStats.getElementCountInMemory(), 3);
                assertEquals(bidsCacheStats.getHitCount(), 0);

                SecondLevelCacheStatistics bidCacheStats =
                        stats.getSecondLevelCacheStatistics(Bid.class.getName());
                assertEquals(bidCacheStats.getElementCountInMemory(), 5);
                assertEquals(bidCacheStats.getHitCount(), 0);

                Set<Bid> bids = item.getBids();
                assertEquals(bids.size(), 3);

                assertEquals(bidsCacheStats.getHitCount(), 1);
                assertEquals(bidCacheStats.getHitCount(), 3);

                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void cacheModes() throws Exception {
        CacheTestData testData = storeTestData();
        Long USER_ID = testData.users.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                Statistics stats =
                        jpa.getEntityManagerFactory()
                                .unwrap(SessionFactory.class)
                                .getStatistics();

                SecondLevelCacheStatistics itemCacheStats =
                        stats.getSecondLevelCacheStatistics(Item.class.getName());
                {
                    Map<String, Object> properties = new HashMap<String, Object>();
                    properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    Item item = em.find(Item.class, ITEM_ID, properties); // Hit the database
                    assertEquals(itemCacheStats.getHitCount(), 0);
                }

                assertEquals(itemCacheStats.getElementCountInMemory(), 3);
                em.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);

                Item item = new Item(
                        // ...
                        "Some Item",
                        CalendarUtil.TOMORROW.getTime(),
                        em.find(User.class, USER_ID)
                );

                em.persist(item);

                em.flush();
                assertEquals(itemCacheStats.getElementCountInMemory(), 3);

                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void cacheNaturalId() throws Exception {
        CacheTestData testData = storeTestData();
        Long USER_ID = testData.users.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {

            Statistics stats =
                    jpa.getEntityManagerFactory()
                            .unwrap(SessionFactory.class)
                            .getStatistics();
            jpa.getEntityManagerFactory().getCache()
                    .unwrap(org.hibernate.Cache.class)
                    .evictNaturalIdRegions();

            jpa.getEntityManagerFactory().getCache().evict(User.class);

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                Session session = em.unwrap(Session.class);

                NaturalIdCacheStatistics userIdStats =
                        stats.getNaturalIdCacheStatistics(User.class.getName() + "##NaturalId");

                assertEquals(userIdStats.getElementCountInMemory(), 0);

                User user = (User) session.byNaturalId(User.class)
                        .using("username", "tester")
                        .load();

                assertNotNull(user);

                assertEquals(userIdStats.getHitCount(), 0);
                assertEquals(userIdStats.getMissCount(), 1);
                assertEquals(userIdStats.getElementCountInMemory(), 1);

                SecondLevelCacheStatistics userStats =
                        stats.getSecondLevelCacheStatistics(User.class.getName());
                assertEquals(userStats.getHitCount(), 0);
                assertEquals(userStats.getMissCount(), 1);
                assertEquals(userStats.getElementCountInMemory(), 1);

                tx.commit();
                em.close();
            }

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                Session session = em.unwrap(Session.class);

                NaturalIdCacheStatistics userIdStats =
                        stats.getNaturalIdCacheStatistics(User.class.getName() + "##NaturalId");
                assertEquals(userIdStats.getElementCountInMemory(), 1);

                User user = (User) session.byNaturalId(User.class)
                        .using("username", "tester")
                        .load();

                assertNotNull(user);

                assertEquals(userIdStats.getHitCount(), 1);

                SecondLevelCacheStatistics userStats =
                        stats.getSecondLevelCacheStatistics(User.class.getName());
                assertEquals(userStats.getHitCount(), 1);

                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void cacheControl() throws Exception {
        CacheTestData testData = storeTestData();
        Long USER_ID = testData.users.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();

        EntityManagerFactory emf = jpa.getEntityManagerFactory();
        Cache cache = emf.getCache();

        assertTrue(cache.contains(Item.class, ITEM_ID));
        cache.evict(Item.class, ITEM_ID);
        cache.evict(Item.class);
        cache.evictAll();

        org.hibernate.Cache hibernateCache =
                cache.unwrap(org.hibernate.Cache.class);

        assertFalse(hibernateCache.containsEntity(Item.class, ITEM_ID));
        hibernateCache.evictEntityRegions();
        hibernateCache.evictCollectionRegions();
        hibernateCache.evictNaturalIdRegions();
        hibernateCache.evictQueryRegions();
    }

    @Test
    public void cacheQueryResults() throws Exception {
        CacheTestData testData = storeTestData();
        Long USER_ID = testData.users.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            jpa.getEntityManagerFactory().getCache().evict(Item.class);

            Statistics stats =
                    jpa.getEntityManagerFactory()
                            .unwrap(SessionFactory.class)
                            .getStatistics();

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                String queryString = "select i from Item i where i.name like :n";

                Query query = em.createQuery(queryString)
                        .setParameter("n", "I%")
                        .setHint("org.hibernate.cacheable", true);

                List<Item> items = query.getResultList();
                assertEquals(items.size(), 3);

                QueryStatistics queryStats = stats.getQueryStatistics(queryString);
                assertEquals(queryStats.getCacheHitCount(), 0);
                assertEquals(queryStats.getCacheMissCount(), 1);
                assertEquals(queryStats.getCachePutCount(), 1);

                SecondLevelCacheStatistics itemCacheStats =
                        stats.getSecondLevelCacheStatistics(Item.class.getName());
                assertEquals(itemCacheStats.getElementCountInMemory(), 3);

                tx.commit();
                em.close();
            }

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                String queryString = "select i from Item i where i.name like :n";

                List<Item> items = em.createQuery(queryString)
                        .setParameter("n", "I%")
                        .setHint("org.hibernate.cacheable", true)
                        .getResultList();

                assertEquals(items.size(), 3);

                QueryStatistics queryStats = stats.getQueryStatistics(queryString);
                assertEquals(queryStats.getCacheHitCount(), 1);
                assertEquals(queryStats.getCacheMissCount(), 1);
                assertEquals(queryStats.getCachePutCount(), 1);

                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }

    @javax.management.MXBean
    public interface StatisticsMXBean extends Statistics {
    }

    public void exposeStatistics(final Statistics statistics) throws Exception {
        statistics.setStatisticsEnabled(true);
        Object statisticsBean = Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{StatisticsMXBean.class}, (proxy, method, args) -> method.invoke(statistics, args));
        ManagementFactory.getPlatformMBeanServer()
                .registerMBean(
                        statisticsBean,
                        new ObjectName("org.hibernate:type=statistics")
                );
    }
}
