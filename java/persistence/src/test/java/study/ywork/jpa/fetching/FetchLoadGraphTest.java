package study.ywork.jpa.fetching;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.fetching.fetchloadgraph.Bid;
import study.ywork.jpa.model.fetching.fetchloadgraph.Bid_;
import study.ywork.jpa.model.fetching.fetchloadgraph.Item;
import study.ywork.jpa.model.fetching.fetchloadgraph.Item_;
import study.ywork.jpa.model.fetching.fetchloadgraph.User;
import study.ywork.jpa.share.FetchTestLoadEventListener;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.persistence.Subgraph;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class FetchLoadGraphTest extends JpaManager {
    FetchTestLoadEventListener loadEventListener;

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("fetchingFetchLoadGraph");
    }

    @Override
    public void afterJPABootstrap() {
        loadEventListener = new FetchTestLoadEventListener(jpa.getEntityManagerFactory());
    }

    public FetchTestData storeTestData() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];
        Long[] bidIds = new Long[3];

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
            bidIds[i - 1] = bid.getId();
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
        testData.bids = new TestData(bidIds);
        testData.users = new TestData(userIds);
        return testData;
    }

    @Test
    public void loadItem() throws Exception {
        FetchTestData testData = storeTestData();
        long itemId = testData.items.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                Map<String, Object> properties = new HashMap<>();
                properties.put(
                        "javax.persistence.loadgraph",
                        em.getEntityGraph(Item.class.getSimpleName())
                );

                Item item = em.find(Item.class, itemId, properties);

                assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertFalse(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);

                Map<String, Object> properties = new HashMap<>();
                properties.put("javax.persistence.loadgraph", itemGraph);

                Item item = em.find(Item.class, itemId, properties);

                assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertFalse(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
    }

    @Test
    public void loadItemSeller() throws Exception {
        FetchTestData testData = storeTestData();
        long itemId = testData.items.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                Map<String, Object> properties = new HashMap<>();
                properties.put(
                        "javax.persistence.loadgraph",
                        em.getEntityGraph("ItemSeller")
                );

                Item item = em.find(Item.class, itemId, properties);
                assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertTrue(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
                itemGraph.addAttributeNodes(Item_.seller);

                Map<String, Object> properties = new HashMap<>();
                properties.put("javax.persistence.loadgraph", itemGraph);

                Item item = em.find(Item.class, itemId, properties);
                assertTrue(persistenceUtil.isLoaded(item));
                assertTrue(persistenceUtil.isLoaded(item, "name"));
                assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                assertTrue(persistenceUtil.isLoaded(item, "seller"));
                assertFalse(persistenceUtil.isLoaded(item, "bids"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
                itemGraph.addAttributeNodes("seller");

                List<Item> items =
                        em.createQuery("select i from Item i")
                                .setHint("javax.persistence.loadgraph", itemGraph)
                                .getResultList();

                assertEquals(items.size(), 3);

                for (Item item : items) {
                    assertTrue(persistenceUtil.isLoaded(item));
                    assertTrue(persistenceUtil.isLoaded(item, "name"));
                    assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
                    assertTrue(persistenceUtil.isLoaded(item, "seller"));
                    assertFalse(persistenceUtil.isLoaded(item, "bids"));
                }

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
    }

    @Test
    public void loadBidBidderItem() throws Exception {
        FetchTestData testData = storeTestData();
        long bidId = testData.bids.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                Map<String, Object> properties = new HashMap<>();
                properties.put(
                        "javax.persistence.loadgraph",
                        em.getEntityGraph("BidBidderItem")
                );

                Bid bid = em.find(Bid.class, bidId, properties);

                assertTrue(persistenceUtil.isLoaded(bid));
                assertTrue(persistenceUtil.isLoaded(bid, "amount"));
                assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
                assertTrue(persistenceUtil.isLoaded(bid, "item"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
                assertFalse(persistenceUtil.isLoaded(bid.getItem(), "seller"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
                bidGraph.addAttributeNodes("bidder", "item");

                Map<String, Object> properties = new HashMap<>();
                properties.put("javax.persistence.loadgraph", bidGraph);

                Bid bid = em.find(Bid.class, bidId, properties);

                assertTrue(persistenceUtil.isLoaded(bid));
                assertTrue(persistenceUtil.isLoaded(bid, "amount"));
                assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
                assertTrue(persistenceUtil.isLoaded(bid, "item"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
                assertFalse(persistenceUtil.isLoaded(bid.getItem(), "seller"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
    }

    @Test
    public void loadBidBidderItemSellerBids() throws Exception {
        FetchTestData testData = storeTestData();
        long bidId = testData.bids.getFirstId();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        loadEventListener.reset();
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                Map<String, Object> properties = new HashMap<>();
                properties.put(
                        "javax.persistence.loadgraph",
                        em.getEntityGraph("BidBidderItemSellerBids")
                );

                Bid bid = em.find(Bid.class, bidId, properties);

                assertTrue(persistenceUtil.isLoaded(bid));
                assertTrue(persistenceUtil.isLoaded(bid, "amount"));
                assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
                assertTrue(persistenceUtil.isLoaded(bid, "item"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "seller"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem().getSeller(), "username"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "bids"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
                bidGraph.addAttributeNodes(Bid_.bidder, Bid_.item);
                Subgraph<Item> itemGraph = bidGraph.addSubgraph(Bid_.item);
                itemGraph.addAttributeNodes(Item_.seller, Item_.bids);

                Map<String, Object> properties = new HashMap<>();
                properties.put("javax.persistence.loadgraph", bidGraph);

                Bid bid = em.find(Bid.class, bidId, properties);

                assertTrue(persistenceUtil.isLoaded(bid));
                assertTrue(persistenceUtil.isLoaded(bid, "amount"));
                assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
                assertTrue(persistenceUtil.isLoaded(bid, "item"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "seller"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem().getSeller(), "username"));
                assertTrue(persistenceUtil.isLoaded(bid.getItem(), "bids"));

                tx.commit();
                em.close();
            } finally {
                tm.rollback();
            }
        }
    }
}
