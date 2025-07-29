package study.ywork.jpa.querying.criteria;

import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.LogRecord;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class JoinsTest extends QueryingTest {
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
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(b).where(
                        cb.like(
                                b.get("item").get("name"),
                                "Fo%"
                        )
                );

                Query q = em.createQuery(criteria);
                List<Bid> result = q.getResultList();
                assertEquals(result.size(), 3);
                for (Bid bid : result) {
                    assertEquals(bid.getItem().getId(), testData.items.getFirstId());
                }
            }
            em.clear();
            {
                CriteriaQuery<Bid> criteria = cb.createQuery(Bid.class);
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(b).where(
                        cb.equal(
                                b.get("item").get("seller").get("username"),
                                "tester"
                        )
                );

                Query q = em.createQuery(criteria);
                List<Bid> result = q.getResultList();
                assertEquals(result.size(), 4);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Bid> b = criteria.from(Bid.class);
                criteria.select(b).where(
                        cb.and(
                                cb.equal(
                                        b.get("item").get("seller").get("username"),
                                        "tester"
                                ),
                                cb.isNotNull(b.get("item").get("buyNowPrice"))
                        )
                );

                Query q = em.createQuery(criteria);
                List<Bid> result = q.getResultList();
                assertEquals(result.size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Join<Item, Bid> b = i.join("bids");
                criteria.select(i).where(
                        cb.gt(b.<BigDecimal>get("amount"), new BigDecimal(100))
                );

                Query q = em.createQuery(criteria);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 1);
                assertEquals(result.get(0).getId(), testData.items.getFirstId());
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Join<Item, Bid> b = i.join("bids", JoinType.LEFT);
                b.on(
                        cb.gt(b.<BigDecimal>get("amount"), new BigDecimal(100))
                );
                criteria.multiselect(i, b);

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 3);
                assertTrue(result.get(0)[0] instanceof Item);
                assertTrue(result.get(0)[1] instanceof Bid);
                assertTrue(result.get(1)[0] instanceof Item);
                assertEquals(result.get(1)[1], null);
                assertTrue(result.get(2)[0] instanceof Item);
                assertEquals(result.get(2)[1], null);
            }

            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                i.fetch("bids", JoinType.LEFT);
                criteria.select(i);

                Query q = em.createQuery(criteria);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 5);
                Set<Item> distinctResult = new LinkedHashSet<Item>(result);
                assertEquals(distinctResult.size(), 3);

                boolean haveBids = false;
                for (Item item : distinctResult) {
                    em.detach(item);
                    if (item.getBids().size() > 0) {
                        haveBids = true;
                        break;
                    }
                }
                assertTrue(haveBids);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                i.fetch("bids", JoinType.LEFT);
                criteria.select(i).distinct(true);

                Query q = em.createQuery(criteria);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 3);
                boolean haveBids = false;
                for (Item item : result) {
                    em.detach(item);
                    if (item.getBids().size() > 0) {
                        haveBids = true;
                        break;
                    }
                }
                assertTrue(haveBids);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Fetch<Item, Bid> b = i.fetch("bids", JoinType.LEFT);
                b.fetch("bidder");
                i.fetch("seller", JoinType.LEFT);
                criteria.select(i).distinct(true);

                Query q = em.createQuery(criteria);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 2);
                boolean haveBids = false;
                boolean haveBidder = false;
                boolean haveSeller = false;
                for (Item item : result) {
                    em.detach(item);
                    if (item.getBids().size() > 0) {
                        haveBids = true;
                        Bid bid = item.getBids().iterator().next();
                        if (bid.getBidder() != null && bid.getBidder().getUsername() != null) {
                            haveBidder = true;
                        }
                    }
                    if (item.getSeller() != null && item.getSeller().getUsername() != null)
                        haveSeller = true;
                }
                assertTrue(haveBids);
                assertTrue(haveBidder);
                assertTrue(haveSeller);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                i.fetch("bids", JoinType.LEFT);
                i.fetch("images", JoinType.LEFT);
                criteria.select(i).distinct(true);

                Query q = em.createQuery(criteria);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 3);
                boolean haveBids = false;
                boolean haveImages = false;
                for (Item item : result) {
                    em.detach(item);
                    if (item.getBids().size() > 0)
                        haveBids = true;
                    if (item.getImages().size() > 0)
                        haveImages = true;
                }
                assertTrue(haveBids);
                assertTrue(haveImages);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<User> u = criteria.from(User.class);
                Root<LogRecord> log = criteria.from(LogRecord.class);
                criteria.where(
                        cb.equal(u.get("username"), log.get("username")));
                criteria.multiselect(u, log);

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof User);
                    assertTrue(row[1] instanceof LogRecord);
                }
                assertEquals(result.size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Root<Bid> b = criteria.from(Bid.class);
                criteria.where(
                        cb.equal(b.get("item"), i),
                        cb.equal(i.get("seller"), b.get("bidder"))
                );
                criteria.multiselect(i, b);

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 0);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Root<User> u = criteria.from(User.class);
                criteria.where(
                        cb.equal(i.get("seller"), u),
                        cb.like(u.<String>get("username"), "j%")
                );
                criteria.multiselect(i, u);

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 3);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof Item);
                    assertTrue(row[1] instanceof User);
                }
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                Root<User> u = criteria.from(User.class);
                criteria.where(
                        cb.equal(i.get("seller").get("id"), u.get("id")),
                        cb.like(u.<String>get("username"), "j%")
                );
                criteria.multiselect(i, u);

                Query q = em.createQuery(criteria);
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 3);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof Item);
                    assertTrue(row[1] instanceof User);
                }
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.where(
                        cb.equal(
                                i.get("seller"),
                                cb.parameter(User.class, "seller")
                        )
                );
                criteria.select(i);

                Query q = em.createQuery(criteria);
                User someUser = em.find(User.class, testData.users.getFirstId());
                q.setParameter("seller", someUser);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.where(
                        cb.equal(
                                i.get("seller").get("id"),
                                cb.parameter(Long.class, "sellerId")
                        )
                );
                criteria.select(i);

                Query q = em.createQuery(criteria);
                Long USER_ID = testData.users.getFirstId();
                q.setParameter("sellerId", USER_ID);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 2);
            }
            em.clear();
            {
                CriteriaQuery<Bid> criteria = cb.createQuery(Bid.class);
                Root<Bid> b = criteria.from(Bid.class);
                criteria.where(
                        cb.equal(
                                b.get("item").get("id"),
                                cb.parameter(Long.class, "itemId")
                        )
                );
                criteria.select(b);

                Query q = em.createQuery(criteria);
                q.setParameter("itemId", testData.items.getFirstId());
                List<Bid> result = q.getResultList();
                assertEquals(result.size(), 3);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
