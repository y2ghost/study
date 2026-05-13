package study.ywork.jpa.querying;

import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.inheritance.tableperclass.BankAccount;
import study.ywork.jpa.model.inheritance.tableperclass.CreditCard;
import study.ywork.jpa.model.querying.Address;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Category;
import study.ywork.jpa.model.querying.Image;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.LogRecord;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.share.util.CalendarUtil;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

public class QueryingTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit(
                "querying",
                "querying/ExternalizedQueries.hbm.xml",
                "querying/SQLQueries.hbm.xml",
                "querying/StoredProcedures.hbm.xml"
        );
    }

    public static class TestDataCategoriesItems {
        public TestData categories;
        public TestData items;
        public TestData users;
    }

    public TestDataCategoriesItems storeTestData() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        Long[] categoryIds = new Long[4];
        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];

        User tester = new User("tester", "John", "Doe");
        Address homeAddress = new Address("Some Street 123", "12345", "Some City");
        tester.setActivated(true);
        tester.setHomeAddress(homeAddress);
        em.persist(tester);
        userIds[0] = tester.getId();

        User dev = new User("dev", "Jane", "Roe");
        dev.setActivated(true);
        dev.setHomeAddress(new Address("Other Street 11", "1234", "Other City"));
        em.persist(dev);
        userIds[1] = dev.getId();

        User manager = new User("manager", "Robert", "Doe");
        em.persist(manager);
        userIds[2] = manager.getId();

        Category categoryOne = new Category("One");
        em.persist(categoryOne);
        categoryIds[0] = categoryOne.getId();

        Item item = new Item("Foo", CalendarUtil.TOMORROW.getTime(), tester);
        item.setBuyNowPrice(new BigDecimal("19.99"));
        em.persist(item);
        itemIds[0] = item.getId();
        categoryOne.getItems().add(item);
        item.getCategories().add(categoryOne);
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(item, manager, new BigDecimal(98 + i));
            item.getBids().add(bid);
            em.persist(bid);
        }
        item.getImages().add(new Image("Foo", "foo.jpg", 640, 480));
        item.getImages().add(new Image("Bar", "bar.jpg", 800, 600));
        item.getImages().add(new Image("Baz", "baz.jpg", 1024, 768));

        Category categoryTwo = new Category("Two");
        categoryTwo.setParent(categoryOne);
        em.persist(categoryTwo);
        categoryIds[1] = categoryTwo.getId();

        item = new Item("Bar", CalendarUtil.TOMORROW.getTime(), tester);
        em.persist(item);
        itemIds[1] = item.getId();
        categoryTwo.getItems().add(item);
        item.getCategories().add(categoryTwo);
        Bid bid = new Bid(item, dev, new BigDecimal("4.99"));
        item.getBids().add(bid);
        em.persist(bid);

        item = new Item("Baz", CalendarUtil.AFTER_TOMORROW.getTime(), dev);
        item.setApproved(false);
        em.persist(item);
        itemIds[2] = item.getId();
        categoryTwo.getItems().add(item);
        item.getCategories().add(categoryTwo);

        Category categoryThree = new Category("Three");
        categoryThree.setParent(categoryOne);
        em.persist(categoryThree);
        categoryIds[2] = categoryThree.getId();

        Category categoryFour = new Category("Four");
        categoryFour.setParent(categoryTwo);
        em.persist(categoryFour);
        categoryIds[3] = categoryFour.getId();

        CreditCard cc = new CreditCard(
                "John Doe", "1234123412341234", "06", "2015"
        );
        em.persist(cc);

        BankAccount ba = new BankAccount(
                "Jane Roe", "445566", "One Percent Bank Inc.", "999"
        );
        em.persist(ba);

        LogRecord lr = new LogRecord("tester", "This is a log message");
        em.persist(lr);
        lr = new LogRecord("tester", "Another log message");
        em.persist(lr);

        tx.commit();
        em.close();

        TestDataCategoriesItems testData = new TestDataCategoriesItems();
        testData.categories = new TestData(categoryIds);
        testData.items = new TestData(itemIds);
        testData.users = new TestData(userIds);
        return testData;
    }
}
