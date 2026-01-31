package study.ywork.jpa.fetching;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.fetching.eagerselect.Bid;
import study.ywork.jpa.model.fetching.eagerselect.Item;
import study.ywork.jpa.model.fetching.eagerselect.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class EagerSelectTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("fetchingEagerSelect");
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
    public void fetchEagerSelect() throws Exception {
        FetchTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Long itemId = testData.items.getFirstId();
            Item item = em.find(Item.class, itemId);
            em.detach(item);
            assertEquals(item.getBids().size(), 3);
            assertNotNull(item.getBids().iterator().next().getAmount());
            assertEquals(item.getSeller().getUsername(), "tester");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
