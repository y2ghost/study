package study.ywork.jpa.querying;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.share.util.CalendarUtil;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CreateExecuteQueriesTest extends QueryingTest {
    @Test
    public void createQueries() throws Exception {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Query query = em.createQuery("select i from Item i");
                assertEquals(query.getResultList().size(), 3);
            }
            {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery criteria = cb.createQuery();
                criteria.select(criteria.from(Item.class));

                Query query = em.createQuery(criteria);

                assertEquals(query.getResultList().size(), 3);
            }
            {
                Query query = em.createNativeQuery(
                        "select * from ITEM", Item.class
                );

                assertEquals(query.getResultList().size(), 3);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void createTypedQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        Long ITEM_ID = testData.items.getFirstId();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Query query = em.createQuery(
                        "select i from Item i where i.id = :id"
                ).setParameter("id", ITEM_ID);

                Item result = (Item) query.getSingleResult();

                assertEquals(result.getId(), ITEM_ID);
            }
            {
                TypedQuery<Item> query = em.createQuery(
                        "select i from Item i where i.id = :id", Item.class
                ).setParameter("id", ITEM_ID);

                Item result = query.getSingleResult();

                assertEquals(result.getId(), ITEM_ID);
            }
            {
                CriteriaBuilder cb = em.getCriteriaBuilder();

                CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(cb.equal(i.get("id"), ITEM_ID));

                TypedQuery<Item> query = em.createQuery(criteria);

                Item result = query.getSingleResult();

                assertEquals(result.getId(), ITEM_ID);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void createHibernateQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        Long ITEM_ID = testData.items.getFirstId();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Session session = em.unwrap(Session.class);
                org.hibernate.Query query = session.createQuery("select i from Item i");
                assertEquals(query.list().size(), 3);
            }
            {
                Session session = em.unwrap(Session.class);

                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select {i.*} from ITEM {i}"
                ).addEntity("i", Item.class);

                assertEquals(query.list().size(), 3);
            }
            {
                Session session = em.unwrap(Session.class);

                org.hibernate.Criteria query = session.createCriteria(Item.class);
                query.add(org.hibernate.criterion.Restrictions.eq("id", ITEM_ID));

                Item result = (Item) query.uniqueResult();

                assertEquals(result.getId(), ITEM_ID);
            }
            {
                javax.persistence.Query query = em.createQuery(
                        "select i from Item i"
                );

                org.hibernate.Query hibernateQuery =
                        query.unwrap(org.hibernate.jpa.HibernateQuery.class)
                                .getHibernateQuery();

                hibernateQuery.getQueryString();
                hibernateQuery.getReturnAliases();
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void parameterBinding() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            List<Item> items;
            {
                String searchString = getValueEnteredByUser();
                Query query = em.createQuery(
                        "select i from Item i where i.name = '" + searchString + "'"
                );
                items = query.getResultList();
                assertEquals(items.size(), 0);
            }

            {
                String searchString = "Foo";
                Query query = em.createQuery(
                        "select i from Item i where i.name = :itemName"
                ).setParameter("itemName", searchString);

                for (Parameter<?> parameter : query.getParameters()) {
                    assertTrue(query.isBound(parameter));
                }

                items = query.getResultList();
                assertEquals(items.size(), 1);
            }
            {
                Date tomorrowDate = CalendarUtil.TOMORROW.getTime();

                Query query = em.createQuery(
                        "select i from Item i where i.auctionEnd > :endDate"
                ).setParameter("endDate", tomorrowDate, TemporalType.TIMESTAMP);

                items = query.getResultList();
                assertEquals(items.size(), 1);
            }
            {
                Item someItem = em.find(Item.class, testData.items.getFirstId());

                Query query = em.createQuery(
                        "select b from Bid b where b.item = :item"
                ).setParameter("item", someItem);

                items = query.getResultList();
                assertEquals(items.size(), 3);
            }
            {
                String searchString = "Foo";
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);

                Query query = em.createQuery(
                        criteria.select(i).where(
                                cb.equal(
                                        i.get("name"),
                                        cb.parameter(String.class, "itemName")
                                )
                        )
                ).setParameter("itemName", searchString);

                items = query.getResultList();
                assertEquals(items.size(), 1);
            }
            {
                String searchString = "Foo";
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery criteria = cb.createQuery(Item.class);
                Root<Item> i = criteria.from(Item.class);

                ParameterExpression<String> itemNameParameter =
                        cb.parameter(String.class);

                Query query = em.createQuery(
                        criteria.select(i).where(
                                cb.equal(
                                        i.get("name"),
                                        itemNameParameter
                                )
                        )
                ).setParameter(itemNameParameter, searchString);

                items = query.getResultList();
                assertEquals(items.size(), 1);
            }
            {
                String searchString = "B%";
                Date tomorrowDate = CalendarUtil.TOMORROW.getTime();

                Query query = em.createQuery(
                        "select i from Item i where i.name like ?1 and i.auctionEnd > ?2"
                );
                query.setParameter(1, searchString);
                query.setParameter(2, tomorrowDate, TemporalType.TIMESTAMP);

                items = query.getResultList();
                assertEquals(items.size(), 1);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void pagination() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            List<Item> items;
            {
                Query query = em.createQuery("select i from Item i");
                query.setFirstResult(40).setMaxResults(10);

                items = query.getResultList();
                assertEquals(items.size(), 0);
            }
            {
                Query query = em.createNativeQuery("select * from ITEM");
                query.setFirstResult(40).setMaxResults(10);

                items = query.getResultList();
                assertEquals(items.size(), 0);
            }
            {
                Query query = em.createQuery("select i from Item i");

                org.hibernate.Query hibernateQuery =
                        query.unwrap(org.hibernate.jpa.HibernateQuery.class).getHibernateQuery();

                org.hibernate.ScrollableResults cursor =
                        hibernateQuery.scroll(org.hibernate.ScrollMode.SCROLL_INSENSITIVE);

                cursor.last();
                int count = cursor.getRowNumber() + 1;

                cursor.close();

                query.setFirstResult(40).setMaxResults(10);

                assertEquals(count, 3);
                items = query.getResultList();
                assertEquals(items.size(), 0);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Query query = em.createQuery("select i from Item i");
                List<Item> items = query.getResultList();

                assertEquals(items.size(), 3);
            }
            {
                Query query = em.createQuery("select i.name from Item i");
                List<String> itemNames = query.getResultList();

                assertEquals(itemNames.size(), 3);
            }
            {
                TypedQuery<Item> query = em.createQuery(
                        "select i from Item i where i.id = :id", Item.class
                ).setParameter("id", ITEM_ID);

                Item item = query.getSingleResult();

                assertEquals(item.getId(), ITEM_ID);
            }
            {
                TypedQuery<String> query = em.createQuery(
                        "select i.name from Item i where i.id = :id", String.class
                ).setParameter("id", ITEM_ID);

                String itemName = query.getSingleResult();

                assertEquals(em.find(Item.class, ITEM_ID).getName(), itemName);
            }
            {
                boolean gotException = false;
                try {
                    TypedQuery<Item> query = em.createQuery(
                            "select i from Item i where i.id = :id", Item.class
                    ).setParameter("id", 1234l);

                    Item item = query.getSingleResult();

                } catch (NoResultException ex) {
                    gotException = true;
                }
                assertTrue(gotException);
            }
            {
                boolean gotException = false;
                try {
                    Query query = em.createQuery(
                            "select i from Item i where name like '%a%'"
                    );

                    Item item = (Item) query.getSingleResult();
                } catch (NonUniqueResultException ex) {
                    gotException = true;
                }
                assertTrue(gotException);
            }
            {
                Session session = em.unwrap(Session.class);

                org.hibernate.Query query = session.createQuery(
                        "select i from Item i order by i.id asc"
                );

                org.hibernate.ScrollableResults cursor =
                        query.scroll(org.hibernate.ScrollMode.SCROLL_INSENSITIVE);

                cursor.setRowNumber(2);

                Item item = (Item) cursor.get(0);

                cursor.close();

                assertEquals(item.getName(), "Baz");
            }
            {
                Session session = em.unwrap(Session.class);

                org.hibernate.Query query = session.createQuery(
                        "select i from Item i"
                );

                int count = 0;
                Iterator<Item> it = query.iterate();
                while (it.hasNext()) {
                    Item next = it.next();
                    count++;
                }
                Hibernate.close(it);
                assertEquals(count, 3);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2", "MARIADB"})
    public void namedQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        Long ITEM_ID = testData.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Session session = em.unwrap(Session.class);

            {
                Query query = em.createNamedQuery("findItems");

                assertEquals(query.getResultList().size(), 3);
            }
            {
                TypedQuery<Item> query = em.createNamedQuery("findItemById", Item.class);

                query.setParameter("id", ITEM_ID);
                Item result = query.getSingleResult();
                assertEquals(result.getId(), ITEM_ID);
            }
            {
                org.hibernate.Query query = session.getNamedQuery("findItems");

                assertEquals(query.list().size(), 3);
            }
            {
                Query query = em.createNamedQuery("findItemsSQL");

                List<Item> items = query.getResultList();
                assertEquals(items.size(), 3);
                assertEquals(items.get(0).getId(), ITEM_ID);
            }
            {
                Query query = em.createNamedQuery("findItemsOrderByName");

                List<Item> items = query.getResultList();
                assertEquals(items.size(), 3);
                assertEquals(items.get(2).getId(), ITEM_ID);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2", "MARIADB"})
    public void createNamedQueries() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Query findItemsQuery = em.createQuery("select i from Item i");
                em.getEntityManagerFactory().addNamedQuery(
                        "savedFindItemsQuery", findItemsQuery
                );

                // Later on... with the same EntityManagerFactory
                Query query = em.createNamedQuery("savedFindItemsQuery");
                assertEquals(query.getResultList().size(), 3);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    protected String getValueEnteredByUser() {
        return "ALWAYS FILTER VALUES ENTERED BY USERS!";
    }

    @Test(groups = {"H2", "MARIADB"})
    public void queryHints() throws Exception {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            String queryString = "select i from Item i";

            {
                Query query = em.createQuery(queryString)
                        .setHint("javax.persistence.query.timeout", 60000);

                assertEquals(query.getResultList().size(), 3);
            }
            {
                Query query = em.createQuery(queryString)
                        .setFlushMode(FlushModeType.COMMIT);

                assertEquals(query.getResultList().size(), 3);
            }
            {
                Query query = em.createQuery(queryString)
                        .setHint(
                                org.hibernate.annotations.QueryHints.READ_ONLY,
                                true
                        );

                assertEquals(query.getResultList().size(), 3);
            }
            {
                Query query = em.createQuery(queryString)
                        .setHint(
                                org.hibernate.annotations.QueryHints.FETCH_SIZE,
                                50
                        );

                assertEquals(query.getResultList().size(), 3);
            }
            {
                Query query = em.createQuery(queryString)
                        .setHint(
                                org.hibernate.annotations.QueryHints.COMMENT,
                                "Custom SQL comment"
                        );

                assertEquals(query.getResultList().size(), 3);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
