package study.ywork.jpa.querying.criteria;

import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.AuctionType;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Category;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;
import study.ywork.jpa.share.util.CalendarUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class RestrictionTest extends QueryingTest {
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
                criteria.select(i).where(
                    cb.equal(i.get("name"), "Foo")
                );

                TypedQuery<Item> q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
                assertEquals(q.getResultList().iterator().next().getName(), "Foo");
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).where(
                    cb.equal(u.get("activated"), true)
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(b).where(
                    cb.between(
                        b.<BigDecimal>get("amount"),
                        new BigDecimal("99"), new BigDecimal("110")
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(b).where(
                    cb.gt(
                        b.<BigDecimal>get("amount"),
                        new BigDecimal("100")
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                Date tomorrowDate = CalendarUtil.TOMORROW.getTime();

                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(
                    cb.greaterThan(
                        i.get("auctionEnd"),
                        tomorrowDate
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).where(
                    cb.in(u.<String>get("username"))
                        .value("tester")
                        .value("dev")
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(
                    cb.equal(
                        i.<AuctionType>get("auctionType"),
                        AuctionType.HIGHEST_BID
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(
                    cb.isNull(i.get("buyNowPrice"))
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(
                    cb.isNotNull(i.get("buyNowPrice"))
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).where(
                    cb.like(u.get("username"), "john%")
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).where(
                    cb.like(u.get("username"), "john%").not()
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).where(
                    cb.like(u.get("username"), "%oe%")
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(
                    cb.like(i.get("name"), "Name\\_with\\_underscores", '\\')
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 0);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(b).where(
                    cb.gt(
                        cb.diff(
                            cb.quot(b.<BigDecimal>get("amount"), 2),
                            0.5
                        ),
                        49
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);

                Predicate predicate = cb.and(
                    cb.like(i.get("name"), "Fo%"),
                    cb.isNotNull(i.get("buyNowPrice"))
                );

                predicate = cb.or(
                    predicate,
                    cb.equal(i.<String>get("name"), "Bar")
                );

                criteria.select(i).where(predicate);
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);

                criteria.select(i).where(
                    cb.like(i.get("name"), "Fo%"),
                    cb.isNotNull(i.get("buyNowPrice"))
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Category> c = criteria.from(Category.class);
                criteria.select(c).where(
                    cb.isNotEmpty(c.<Collection>get("items"))
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Category> c = criteria.from(Category.class);
                criteria.select(c).where(
                    cb.isMember(
                        cb.parameter(Item.class, "item"),
                        c.<Collection<Item>>get("items")
                    )
                );

                Query q = em.createQuery(criteria);
                Item item = em.find(Item.class, testData.items.getFirstId());
                q.setParameter("item", item);
                List<Category> result = q.getResultList();
                assertEquals(result.size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Category> c = criteria.from(Category.class);
                criteria.select(c).where(
                    cb.gt(
                        cb.size(c.<Collection>get("items")),
                        1
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i).where(
                    cb.like(cb.lower(i.get("name")), "ba%")
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).orderBy(
                    cb.desc(u.get("username"))
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                criteria.select(u).orderBy(
                    cb.desc(u.get("activated")),
                    cb.asc(u.get("username"))
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }

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
                criteria.select(i).where(
                    cb.gt(
                        cb.function(
                            "DATEDIFF",
                            Integer.class,
                            cb.literal("DAY"),
                            i.get("createdOn"),
                            i.get("auctionEnd")
                        ),
                        1
                    )
                );

                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
