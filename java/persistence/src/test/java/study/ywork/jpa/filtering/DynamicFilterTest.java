package study.ywork.jpa.filtering;

import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.filtering.dynamic.Category;
import study.ywork.jpa.model.filtering.dynamic.Item;
import study.ywork.jpa.model.filtering.dynamic.User;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class DynamicFilterTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("filteringDynamic");
    }

    class DynamicFilterTestData {
        TestData categories;
        TestData items;
        TestData users;
    }

    public DynamicFilterTestData storeTestData() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        DynamicFilterTestData testData = new DynamicFilterTestData();

        testData.users = new TestData(new Long[2]);
        User tester = new User("tester");
        em.persist(tester);
        testData.users.identifiers[0] = tester.getId();
        User dev = new User("dev", 100);
        em.persist(dev);
        testData.users.identifiers[1] = dev.getId();

        testData.categories = new TestData(new Long[2]);
        Category categoryOne = new Category("One");
        em.persist(categoryOne);
        testData.categories.identifiers[0] = categoryOne.getId();
        Category categoryTwo = new Category("Two");
        em.persist(categoryTwo);
        testData.categories.identifiers[1] = categoryTwo.getId();

        testData.items = new TestData(new Long[3]);
        Item itemFoo = new Item("Foo", categoryOne, tester);
        em.persist(itemFoo);
        testData.items.identifiers[0] = itemFoo.getId();
        Item itemBar = new Item("Bar", categoryOne, dev);
        em.persist(itemBar);
        testData.items.identifiers[1] = itemBar.getId();
        Item itemBaz = new Item("Baz", categoryTwo, dev);
        em.persist(itemBaz);
        testData.items.identifiers[2] = itemBaz.getId();

        tx.commit();
        em.close();
        return testData;
    }

    @Test
    public void filterItems() throws Throwable {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                org.hibernate.Filter filter = em.unwrap(Session.class).enableFilter("limitByUserRank");
                filter.setParameter("currentUserRank", 0);

                {
                    List<Item> items = em.createQuery("select i from Item i").getResultList();
                    assertEquals(items.size(), 1);
                }
                em.clear();
                {
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery criteria = cb.createQuery();
                    criteria.select(criteria.from(Item.class));
                    List<Item> items = em.createQuery(criteria).getResultList();
                    assertEquals(items.size(), 1);
                }
                em.clear();

                filter.setParameter("currentUserRank", 100);
                List<Item> items = em.createQuery("select i from Item i")
                        .getResultList();
                assertEquals(items.size(), 3);
            }

            em.clear();
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void filterCollection() throws Throwable {
        DynamicFilterTestData testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Long categoryId = testData.categories.getFirstId();

            {
                org.hibernate.Filter filter = em.unwrap(Session.class)
                        .enableFilter("limitByUserRank");
                filter.setParameter("currentUserRank", 0);
                Category category = em.find(Category.class, categoryId);
                assertEquals(category.getItems().size(), 1);

                em.clear();
                filter.setParameter("currentUserRank", 100);
                category = em.find(Category.class, categoryId);
                assertEquals(category.getItems().size(), 2);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
