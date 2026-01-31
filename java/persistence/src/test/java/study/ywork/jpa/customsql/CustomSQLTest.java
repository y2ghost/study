package study.ywork.jpa.customsql;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.customsql.Bid;
import study.ywork.jpa.model.customsql.Category;
import study.ywork.jpa.model.customsql.Image;
import study.ywork.jpa.model.customsql.Item;
import study.ywork.jpa.model.customsql.User;
import study.ywork.jpa.share.FetchTestLoadEventListener;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class CustomSQLTest extends JpaManager {
    FetchTestLoadEventListener loadEventListener;

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("customSQL", "customsql/ItemQueries.hbm.xml");
    }

    @Override
    public void afterJPABootstrap() {
        loadEventListener = new FetchTestLoadEventListener(jpa.getEntityManagerFactory());
    }

    class CustomSQLTestData {
        TestData categories;
        TestData items;
        TestData bids;
        TestData users;
    }

    public CustomSQLTestData create() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        CustomSQLTestData testData = new CustomSQLTestData();
        testData.categories = new TestData(new Long[1]);
        testData.items = new TestData(new Long[2]);
        testData.bids = new TestData(new Long[3]);
        testData.users = new TestData(new Long[2]);

        User tester = new User("tester");
        em.persist(tester);
        testData.users.identifiers[0] = tester.getId();

        User dev = new User("dev");
        em.persist(dev);
        testData.users.identifiers[1] = dev.getId();

        Category category = new Category();
        category.setName("Foo");
        em.persist(category);
        testData.categories.identifiers[0] = category.getId();

        Item item = new Item();
        item.setName("Some item");
        item.setCategory(category);
        item.setSeller(tester);
        item.setAuctionEnd(CalendarUtil.TOMORROW.getTime());

        item.getImages().add(
                new Image("foo.jpg", 640, 480)
        );
        item.getImages().add(
                new Image("bar.jpg", 800, 600)
        );
        item.getImages().add(
                new Image("baz.jpg", 640, 480)
        );

        em.persist(item);
        testData.items.identifiers[0] = item.getId();

        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid();
            bid.setAmount(new BigDecimal(10 + i));
            bid.setItem(item);
            bid.setBidder(dev);
            em.persist(bid);
            testData.bids.identifiers[i - 1] = bid.getId();
        }

        Item otherItem = new Item(category, "Inactive item", CalendarUtil.TOMORROW.getTime(), tester);
        otherItem.setActive(false);
        em.persist(otherItem);

        tx.commit();
        em.close();

        return testData;
    }

    @Test
    public void read() throws Exception {
        CustomSQLTestData testData = create();
        Long CATEGORY_ID = testData.categories.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();
        Long BID_ID = testData.bids.getFirstId();
        Long USER_ID = testData.users.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                User user = em.find(User.class, USER_ID);
                assertEquals(loadEventListener.getLoadCount(User.class), 1);
                assertEquals(user.getId(), USER_ID);
            }
            em.clear();
            loadEventListener.reset();

            {
                Bid bid = em.find(Bid.class, BID_ID);
                assertEquals(loadEventListener.getLoadCount(Bid.class), 1);
                assertEquals(loadEventListener.getLoadCount(User.class), 1);
                assertEquals(loadEventListener.getLoadCount(Item.class), 0);
                assertEquals(bid.getId(), BID_ID);
                assertNotNull(bid.getBidder().getUsername());
            }
            em.clear();
            loadEventListener.reset();

            {
                Item item = em.find(Item.class, ITEM_ID);
                assertEquals(loadEventListener.getLoadCount(Item.class), 1);
                assertEquals(loadEventListener.getLoadCount(Bid.class), 3);
                assertEquals(item.getId(), ITEM_ID);
                assertEquals(item.getBids().size(), 3);
                assertEquals(item.getImages().size(), 3);
            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2", "POSTGRESQL"})
    public void readRestrictedCollection() throws Exception {
        CustomSQLTestData testData = create();
        Long CATEGORY_ID = testData.categories.getFirstId();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            {
                Category category = em.find(Category.class, CATEGORY_ID);
                assertEquals(loadEventListener.getLoadCount(Category.class), 1);
                Set<Item> items = category.getItems();
                assertEquals(items.size(), 1);
                assertEquals(items.iterator().next().getId(), ITEM_ID);
            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void update() throws Exception {
        CustomSQLTestData testData = create();
        Long USER_ID = testData.users.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                User user = em.find(User.class, USER_ID);
                user.setUsername("jdoe");
                em.flush();

            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void delete() throws Exception {
        CustomSQLTestData testData = create();
        Long ITEM_ID = testData.items.getFirstId();
        Long USER_ID = testData.users.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Item item = em.find(Item.class, ITEM_ID);
                assertEquals(item.getImages().size(), 3);
                item.getImages().remove(item.getImages().iterator().next());
                em.flush();
            }
            em.clear();
            loadEventListener.reset();

            {
                Item item = em.find(Item.class, ITEM_ID);
                assertEquals(item.getImages().size(), 2);
                item.getImages().clear();
                em.flush();
            }
            em.clear();
            loadEventListener.reset();

            {
                em.createQuery("delete Bid").executeUpdate();
                em.createQuery("delete Item").executeUpdate();
                em.clear();

                User user = em.find(User.class, USER_ID);
                em.remove(user);
                em.flush();
                em.clear();

                assertNull(em.find(User.class, USER_ID));
            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
