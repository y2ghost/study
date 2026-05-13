package study.ywork.jpa.querying.advanced;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Address;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HibernateCriteriaTest extends QueryingTest {
    @Test
    public void foo() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Session session = em.unwrap(Session.class);

            {
                List<Object[]> result =
                        session.createCriteria(Bid.class, "b")
                                .createAlias("item", "i", JoinType.RIGHT_OUTER_JOIN)
                                .add(Restrictions.or(
                                        Restrictions.isNull("b.id"),
                                        Restrictions.gt("amount", new BigDecimal(100))
                                ))
                                .setResultTransformer(Criteria.PROJECTION)
                                .list();

                assertEquals(result.size(), 2);

                assertTrue(result.get(0)[0] instanceof Item);
                assertTrue(result.get(0)[1] instanceof Bid);

                assertTrue(result.get(1)[0] instanceof Item);
                assertEquals(result.get(1)[1], null);
            }
            em.clear();
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }


    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Session session = em.unwrap(Session.class);

            {
                org.hibernate.Criteria criteria = session.createCriteria(Item.class);
                List<Item> items = criteria.list();
                assertEquals(items.size(), 3);
            }
            em.clear();

            {
                DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);

                List<Item> items = criteria.getExecutableCriteria(session).list();
                assertEquals(items.size(), 3);
            }
            em.clear();

            {
                List<User> users =
                        session.createCriteria(User.class)
                                .addOrder(Order.asc("firstname"))
                                .addOrder(Order.asc("lastname"))
                                .list();

                assertEquals(users.size(), 3);
                assertEquals(users.get(0).getFirstname(), "Jane");
                assertEquals(users.get(1).getFirstname(), "John");
                assertEquals(users.get(2).getFirstname(), "Robert");
            }
            em.clear();

            {
                List<Item> items =
                        session.createCriteria(Item.class)
                                .add(Restrictions.eq("name", "Foo"))
                                .list();

                assertEquals(items.size(), 1);
            }
            em.clear();

            {
                List<User> users =
                        session.createCriteria(User.class)
                                .add(Restrictions.like("username", "j", MatchMode.START).ignoreCase())
                                .list();

                assertEquals(users.size(), 2);
            }
            em.clear();

            {
                List<User> users =
                        session.createCriteria(User.class)
                                .add(Restrictions.eq("homeAddress.city", "Some City"))
                                .list();

                assertEquals(users.size(), 1);
                assertEquals(users.get(0).getUsername(), "tester");
            }
            em.clear();

            {
                List<User> users =
                        session.createCriteria(User.class)
                                .add(Restrictions.sqlRestriction(
                                        "length({alias}.USERNAME) < ?",
                                        8,
                                        StandardBasicTypes.INTEGER
                                )).list();

                assertEquals(users.size(), 2);
            }
            em.clear();

            {
                List<Object[]> result =
                        session.createCriteria(User.class)
                                .setProjection(Projections.projectionList()
                                        .add(Projections.property("id"))
                                        .add(Projections.property("username"))
                                        .add(Projections.property("homeAddress"))
                                ).list();

                assertEquals(result.size(), 3);
                for (Object[] tuple : result) {
                    assertTrue(tuple[0] instanceof Long);
                    assertTrue(tuple[1] instanceof String);
                    assertTrue(tuple[2] == null || tuple[2] instanceof Address);
                }
            }
            em.clear();

            {
                List<String> result =
                        session.createCriteria(Item.class)
                                .setProjection(Projections.projectionList()
                                        .add(Projections.sqlProjection(
                                                "NAME || ':' || AUCTIONEND as RESULT",
                                                new String[]{"RESULT"},
                                                new Type[]{StandardBasicTypes.STRING}
                                        ))
                                ).list();

                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                List<Object[]> result =
                        session.createCriteria(User.class)
                                .setProjection(Projections.projectionList()
                                        .add(Projections.groupProperty("lastname"))
                                        .add(Projections.rowCount())
                                ).list();

                assertEquals(result.size(), 2);
                for (Object[] tuple : result) {
                    assertTrue(tuple[0] instanceof String);
                    assertTrue(tuple[1] instanceof Long);
                }
            }
            em.clear();

            {
                List<Object[]> result =
                        session.createCriteria(Bid.class)
                                .setProjection(Projections.projectionList()
                                        .add(Projections.groupProperty("item"))
                                        .add(Projections.avg("amount"))
                                ).list();

                assertEquals(result.size(), 2);
                for (Object[] tuple : result) {
                    assertTrue(tuple[0] instanceof Item);
                    assertTrue(tuple[1] instanceof Double);
                }
            }
            em.clear();

            {
                List<Bid> result =
                        session.createCriteria(Bid.class)
                                .createCriteria("item") // Inner join
                                .add(Restrictions.like(
                                        "name", "Fo", MatchMode.START
                                ))
                                .list();

                assertEquals(result.size(), 3);
                for (Bid bid : result) {
                    assertEquals(bid.getItem().getId(), testData.items.getFirstId());
                }
            }
            em.clear();

            {
                List<Bid> result =
                        session.createCriteria(Bid.class)
                                .createCriteria("item") // Inner join
                                .add(Restrictions.isNotNull("buyNowPrice"))
                                .createCriteria("seller") // Inner join
                                .add(Restrictions.eq("username", "tester"))
                                .list();

                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                List<Object[]> result =
                        session.createCriteria(Bid.class, "b")
                                .createAlias("item", "i", JoinType.RIGHT_OUTER_JOIN)
                                .add(Restrictions.or(
                                        Restrictions.isNull("b.id"),
                                        Restrictions.gt("amount", new BigDecimal(100))
                                ))
                                .setResultTransformer(Criteria.PROJECTION)
                                .list();

                assertEquals(result.size(), 2);

                assertTrue(result.get(0)[0] instanceof Item);
                assertTrue(result.get(0)[1] instanceof Bid);

                assertTrue(result.get(1)[0] instanceof Item);
                assertEquals(result.get(1)[1], null);
            }
            em.clear();

            {
                List<Bid> result =
                        session.createCriteria(Bid.class)
                                .createCriteria("item") // Inner join
                                .createAlias("seller", "s") // Inner join
                                .add(Restrictions.and(
                                        Restrictions.eq("s.username", "tester"),
                                        Restrictions.isNotNull("buyNowPrice")
                                ))
                                .list();

                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                List<Item> result =
                        session.createCriteria(Item.class)
                                .setFetchMode("bids", FetchMode.JOIN)
                                .list();

                assertEquals(result.size(), 5);
                Set<Item> distinctResult = new LinkedHashSet<>(result);
                assertEquals(distinctResult.size(), 3);

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
                List<Item> result =
                        session.createCriteria(Item.class)
                                .setFetchMode("bids", FetchMode.JOIN)
                                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                                .list();

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
                List<Item> result =
                        session.createCriteria(Item.class)
                                .createAlias("bids", "b", JoinType.LEFT_OUTER_JOIN)
                                .setFetchMode("b", FetchMode.JOIN)
                                .createAlias("b.bidder", "bdr", JoinType.INNER_JOIN)
                                .setFetchMode("bdr", FetchMode.JOIN)
                                .createAlias("seller", "s", JoinType.LEFT_OUTER_JOIN)
                                .setFetchMode("s", FetchMode.JOIN)
                                .list();

                result = new ArrayList<Item>(new LinkedHashSet<Item>(result));
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
                DetachedCriteria sq = DetachedCriteria.forClass(Item.class, "i");
                sq.add(Restrictions.eqProperty("i.seller.id", "u.id"));
                sq.setProjection(Projections.rowCount());

                List<User> result =
                        session.createCriteria(User.class, "u")
                                .add(Subqueries.lt(1l, sq))
                                .list();

                assertEquals(result.size(), 1);
                User user = result.iterator().next();
                assertEquals(user.getId(), testData.users.getFirstId());
            }
            em.clear();

            {
                DetachedCriteria sq = DetachedCriteria.forClass(Bid.class, "b");
                sq.add(Restrictions.eqProperty("b.item.id", "i.id"));
                sq.setProjection(Projections.property("amount"));

                List<Item> result =
                        session.createCriteria(Item.class, "i")
                                .add(Subqueries.geAll(new BigDecimal(10), sq))
                                .list();

                assertEquals(result.size(), 2);
            }
            em.clear();

            {
                User template = new User();
                template.setLastname("Doe");
                org.hibernate.criterion.Example example = Example.create(template);
                example.ignoreCase();
                example.enableLike(MatchMode.START);
                example.excludeProperty("activated");

                List<User> users =
                        session.createCriteria(User.class)
                                .add(example)
                                .list();

                assertEquals(users.size(), 2);
            }
            em.clear();

            {
                Item itemTemplate = new Item();
                itemTemplate.setName("B");

                Example exampleItem = Example.create(itemTemplate);
                exampleItem.ignoreCase();
                exampleItem.enableLike(MatchMode.START);
                exampleItem.excludeProperty("auctionType");
                exampleItem.excludeProperty("createdOn");

                User userTemplate = new User();
                userTemplate.setLastname("Doe");

                Example exampleUser = Example.create(userTemplate);
                exampleUser.excludeProperty("activated");

                List<Item> items =
                        session
                                .createCriteria(Item.class)
                                .add(exampleItem)
                                .createCriteria("seller").add(exampleUser)
                                .list();

                assertEquals(items.size(), 1);
                assertTrue(items.get(0).getName().startsWith("B"));
                assertEquals(items.get(0).getSeller().getLastname(), "Doe");

            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
