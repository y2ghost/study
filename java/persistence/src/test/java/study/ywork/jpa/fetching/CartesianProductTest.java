package study.ywork.jpa.fetching;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.fetching.cartesianproduct.Bid;
import study.ywork.jpa.model.fetching.cartesianproduct.Item;
import study.ywork.jpa.model.fetching.cartesianproduct.User;
import study.ywork.jpa.share.FetchTestLoadEventListener;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class CartesianProductTest extends JpaManager {
    FetchTestLoadEventListener loadEventListener;

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("fetchingCartesianProduct");
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
        item.getImages().add("foo.jpg");
        item.getImages().add("bar.jpg");
        item.getImages().add("baz.jpg");
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, new BigDecimal(9 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("Item Two", CalendarUtil.TOMORROW.getTime(), tester);
        item.getImages().add("a.jpg");
        item.getImages().add("b.jpg");
        em.persist(item);
        itemIds[1] = item.getId();
        for (int i = 1; i <= 1; i++) {
            Bid bid = new Bid(item, new BigDecimal(2 + i));
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
    public void fetchCollections() throws Exception {
        FetchTestData testData = storeTestData();
        loadEventListener.reset();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            long itemId = testData.items.getFirstId();
            Item item = em.find(Item.class, itemId);
            assertEquals(loadEventListener.getLoadCount(Item.class), 1);
            assertEquals(loadEventListener.getLoadCount(Bid.class), 3);

            em.detach(item);

            assertEquals(item.getImages().size(), 3);
            assertEquals(item.getBids().size(), 3);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
