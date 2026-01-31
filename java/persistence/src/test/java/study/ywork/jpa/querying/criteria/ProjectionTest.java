package study.ywork.jpa.querying.criteria;

import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Address;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.ItemSummary;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProjectionTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        CriteriaBuilder cb =
            jpa.getEntityManagerFactory().getCriteriaBuilder();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(cb.tuple(i, b));

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 12);

                Set<Item> items = new HashSet();
                Set<Bid> bids = new HashSet();
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof Item);
                    items.add((Item) row[0]);

                    assertTrue(row[1] instanceof Bid);
                    bids.add((Bid) row[1]);
                }
                assertEquals(items.size(), 3);
                assertEquals(bids.size(), 4);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.multiselect(
                    u.get("id"), u.get("username"), u.get("homeAddress")
                );

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 3);

                Object[] firstRow = result.get(0);
                assertTrue(firstRow[0] instanceof Long);
                assertTrue(firstRow[1] instanceof String);
                assertTrue(firstRow[2] instanceof Address);
            }
            em.clear();
            {
                CriteriaQuery<String> criteria = cb.createQuery(String.class);

                criteria.select(
                    criteria.from(Item.class).<String>get("name")
                );
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery<String> criteria = cb.createQuery(String.class);

                criteria.select(
                    criteria.from(Item.class).<String>get("name")
                );
                criteria.distinct(true);
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(
                    cb.construct(
                        ItemSummary.class,
                        i.get("id"), i.get("name"), i.get("auctionEnd")
                    )
                );
                Query q = em.createQuery(criteria);
                List<ItemSummary> result = q.getResultList();
                assertEquals(result.size(), 3);
            }
            em.clear();
            {
                CriteriaQuery<Tuple> criteria = cb.createTupleQuery();

                criteria.multiselect(
                    criteria.from(Item.class).alias("i"),
                    criteria.from(Bid.class).alias("b")
                );

                TypedQuery<Tuple> query = em.createQuery(criteria);
                List<Tuple> result = query.getResultList();

                Set<Item> items = new HashSet();
                Set<Bid> bids = new HashSet();

                for (Tuple tuple : result) {
                    Item item = tuple.get(0, Item.class);
                    Bid bid = tuple.get(1, Bid.class);

                    item = tuple.get("i", Item.class);
                    bid = tuple.get("b", Bid.class);

                    for (TupleElement<?> element : tuple.getElements()) {
                        Class clazz = element.getJavaType();
                        String alias = element.getAlias();
                        Object value = tuple.get(element);
                    }
                    items.add(item);
                    bids.add(bid);
                }
                assertEquals(result.size(), 12);
                assertEquals(items.size(), 3);
                assertEquals(bids.size(), 4);

            }
            em.clear();

            {

                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(
                    cb.concat(
                        cb.concat(i.<String>get("name"), ":"),
                        i.<String>get("auctionEnd")
                    )
                );
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();

            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.multiselect(
                    i.get("name"),
                    cb.coalesce(i.<BigDecimal>get("buyNowPrice"), 0)
                );

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 3);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof String);
                    assertTrue(row[1] instanceof BigDecimal);
                }
            }
            em.clear();

            {
                CriteriaQuery criteria = cb.createQuery();
                criteria.select(
                    cb.count(criteria.from(Item.class))
                );

                Query q = em.createQuery(criteria);
                Long count = (Long) q.getSingleResult();
                assertEquals(count, new Long(3));
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                criteria.select(
                    cb.countDistinct(
                        criteria.from(Item.class).get("name")
                    )
                );
                Query q = em.createQuery(criteria);
                Long count = (Long) q.getSingleResult();
                assertEquals(count, new Long(3));
            }
            em.clear();
            {
                CriteriaQuery<Number> criteria = cb.createQuery(Number.class);
                criteria.select(
                    cb.sum(
                        criteria.from(Bid.class).<BigDecimal>get("amount")
                    )
                );
                Query q = em.createQuery(criteria);
                BigDecimal sum = (BigDecimal) q.getSingleResult();
                assertEquals(sum.compareTo(new BigDecimal("304.99")), 0);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Bid> b = criteria.from(Bid.class);
                criteria.multiselect(
                    cb.min(b.<BigDecimal>get("amount")),
                    cb.max(b.<BigDecimal>get("amount"))
                );
                criteria.where(
                    cb.equal(
                        b.get("item").<Long>get("id"),
                        cb.parameter(Long.class, "itemId")
                    )
                );

                Query q = em.createQuery(criteria);
                q.setParameter("itemId", testData.items.getFirstId());
                Object[] result = (Object[]) q.getSingleResult();
                assertEquals(((BigDecimal) result[0]).compareTo(new BigDecimal("99")), 0);
                assertEquals(((BigDecimal) result[1]).compareTo(new BigDecimal("101")), 0);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2"})
    public void executeQueriesWithFunctions() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        CriteriaBuilder cb =
            jpa.getEntityManagerFactory().getCriteriaBuilder();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.multiselect(
                    i.get("name"),
                    cb.function(
                        "DATEDIFF",
                        Integer.class,
                        cb.literal("DAY"),
                        i.get("createdOn"),
                        i.get("auctionEnd")
                    )
                );

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 3);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof String);
                    assertTrue(row[1] instanceof Integer);
                }
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
