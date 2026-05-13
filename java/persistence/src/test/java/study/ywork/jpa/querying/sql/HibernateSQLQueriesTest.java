package study.ywork.jpa.querying.sql;

import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Category;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.ItemSummary;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class HibernateSQLQueriesTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Session session = em.unwrap(Session.class);

            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select NAME, AUCTIONEND from {h-schema}ITEM"
                );
                List<Object[]> result = query.list();

                for (Object[] tuple : result) {
                    assertTrue(tuple[0] instanceof String);
                    assertTrue(tuple[1] instanceof Date);
                }
                assertEquals(result.size(), 3);
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select * from ITEM"
                );
                query.addEntity(Item.class);

                List<Item> result = query.list();
                assertEquals(result.size(), 3);
                assertNotNull(result.get(0));
            }
            em.clear();
            {
                Long ITEM_ID = testData.items.getFirstId();
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select * from ITEM where ID = ?"
                );
                query.addEntity(Item.class);
                query.setParameter(0, ITEM_ID);

                List<Item> result = query.list();
                assertEquals(result.size(), 1);
                assertEquals(result.get(0).getId(), ITEM_ID);
            }
            em.clear();
            {
                Long ITEM_ID = testData.items.getFirstId();
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select * from ITEM where ID = :id"
                );
                query.addEntity(Item.class);
                query.setParameter("id", ITEM_ID);

                List<Item> result = query.list();
                assertEquals(result.size(), 1);
                assertEquals(result.get(0).getId(), ITEM_ID);
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "i.ID as {i.id}, " +
                                "'Auction: ' || i.NAME as {i.name}, " +
                                "i.CREATEDON as {i.createdOn}, " +
                                "i.AUCTIONEND as {i.auctionEnd}, " +
                                "i.AUCTIONTYPE as {i.auctionType}, " +
                                "i.APPROVED as {i.approved}, " +
                                "i.BUYNOWPRICE as {i.buyNowPrice}, " +
                                "i.SELLER_ID as {i.seller} " +
                                "from ITEM i"
                );
                query.addEntity("i", Item.class);

                List<Item> result = query.list();
                assertEquals(result.size(), 3);
                assertNotNull(result.get(0));
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "i.ID, " +
                                "'Auction: ' || i.NAME as EXTENDED_NAME, " +
                                "i.CREATEDON, " +
                                "i.AUCTIONEND, " +
                                "i.AUCTIONTYPE, " +
                                "i.APPROVED, " +
                                "i.BUYNOWPRICE, " +
                                "i.SELLER_ID " +
                                "from ITEM i"
                );
                query.addRoot("i", Item.class)
                        .addProperty("id", "ID")
                        .addProperty("name", "EXTENDED_NAME")
                        .addProperty("createdOn", "CREATEDON")
                        .addProperty("auctionEnd", "AUCTIONEND")
                        .addProperty("auctionType", "AUCTIONTYPE")
                        .addProperty("approved", "APPROVED")
                        .addProperty("buyNowPrice", "BUYNOWPRICE")
                        .addProperty("seller", "SELLER_ID");

                List<Item> result = query.list();
                assertEquals(result.size(), 3);
                assertNotNull(result.get(0));
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "i.ID, " +
                                "'Auction: ' || i.NAME as EXTENDED_NAME, " +
                                "i.CREATEDON, " +
                                "i.AUCTIONEND, " +
                                "i.AUCTIONTYPE, " +
                                "i.APPROVED, " +
                                "i.BUYNOWPRICE, " +
                                "i.SELLER_ID " +
                                "from ITEM i"
                );
                query.setResultSetMapping("ItemResult");

                List<Item> result = query.list();
                assertEquals(result.size(), 3);
                assertNotNull(result.get(0));

            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "{i.*}, {u.*} " +
                                "from ITEM i join USERS u on u.ID = i.SELLER_ID"
                );
                query.addEntity("i", Item.class);
                query.addEntity("u", User.class);

                List<Object[]> result = query.list();

                for (Object[] tuple : result) {
                    assertTrue(tuple[0] instanceof Item);
                    assertTrue(tuple[1] instanceof User);
                    Item item = (Item) tuple[0];
                    assertTrue(Persistence.getPersistenceUtil().isLoaded(item, "seller"));
                    assertEquals(item.getSeller(), tuple[1]);
                }
                assertEquals(result.size(), 3);
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "u.ID as {u.id}, " +
                                "u.USERNAME as {u.username}, " +
                                "u.FIRSTNAME as {u.firstname}, " +
                                "u.LASTNAME as {u.lastname}, " +
                                "u.ACTIVATED as {u.activated}, " +
                                "u.STREET as {u.homeAddress.street}, " +
                                "u.ZIPCODE as {u.homeAddress.zipcode}, " +
                                "u.CITY as {u.homeAddress.city} " +
                                "from USERS u"
                );
                query.addEntity("u", User.class);

                List<User> result = query.list();
                assertEquals(result.size(), 3);
                assertNotNull(result.get(0));
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "i.ID as ITEM_ID, " +
                                "i.NAME, " +
                                "i.CREATEDON, " +
                                "i.AUCTIONEND, " +
                                "i.AUCTIONTYPE, " +
                                "i.APPROVED, " +
                                "i.BUYNOWPRICE, " +
                                "i.SELLER_ID, " +
                                "b.ID as BID_ID," +
                                "b.ITEM_ID as BID_ITEM_ID, " +
                                "b.AMOUNT, " +
                                "b.BIDDER_ID " +
                                "from ITEM i left outer join BID b on i.ID = b.ITEM_ID"
                );
                query.addRoot("i", Item.class)
                        .addProperty("id", "ITEM_ID")
                        .addProperty("name", "NAME")
                        .addProperty("createdOn", "CREATEDON")
                        .addProperty("auctionEnd", "AUCTIONEND")
                        .addProperty("auctionType", "AUCTIONTYPE")
                        .addProperty("approved", "APPROVED")
                        .addProperty("buyNowPrice", "BUYNOWPRICE")
                        .addProperty("seller", "SELLER_ID");

                query.addFetch("b", "i", "bids")
                        .addProperty("key", "BID_ITEM_ID")
                        .addProperty("element", "BID_ID")
                        .addProperty("element.id", "BID_ID")
                        .addProperty("element.item", "BID_ITEM_ID")
                        .addProperty("element.amount", "AMOUNT")
                        .addProperty("element.bidder", "BIDDER_ID");

                List<Object[]> result = query.list();

                assertEquals(result.size(), 5);

                for (Object[] tuple : result) {
                    Item item = (Item) tuple[0];
                    assertTrue(Persistence.getPersistenceUtil().isLoaded(item, "bids"));
                    Bid bid = (Bid) tuple[1];
                    if (bid != null)
                        assertTrue(item.getBids().contains(bid));
                }
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "{i.*}, " +
                                "{b.*} " +
                                "from ITEM i left outer join BID b on i.ID = b.ITEM_ID"
                );
                query.addEntity("i", Item.class);
                query.addFetch("b", "i", "bids");

                List<Object[]> result = query.list();
                assertEquals(result.size(), 5);
                for (Object[] tuple : result) {
                    Item item = (Item) tuple[0];
                    assertTrue(Persistence.getPersistenceUtil().isLoaded(item, "bids"));
                    Bid bid = (Bid) tuple[1];
                    if (bid != null)
                        assertTrue(item.getBids().contains(bid));
                }
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select " +
                                "i.*, " +
                                "count(b.ID) as NUM_OF_BIDS " +
                                "from ITEM i left join BID b on b.ITEM_ID = i.ID " +
                                "group by i.ID, i.NAME, i.CREATEDON, i.AUCTIONEND, " +
                                "i.AUCTIONTYPE, i.APPROVED, i.BUYNOWPRICE, i.SELLER_ID"
                );
                query.addEntity(Item.class);
                query.addScalar("NUM_OF_BIDS");

                List<Object[]> result = query.list();

                for (Object[] tuple : result) {
                    assertTrue(tuple[0] instanceof Item);
                    assertTrue(tuple[1] instanceof Number);
                }
                assertEquals(result.size(), 3);
                assertNotNull(result.get(0));
            }
            em.clear();
            {
                org.hibernate.SQLQuery query = session.createSQLQuery(
                        "select ID, NAME, AUCTIONEND from ITEM"
                );

                query.addScalar("ID", StandardBasicTypes.LONG);
                query.addScalar("NAME");
                query.addScalar("AUCTIONEND");

                query.setResultTransformer(
                        new AliasToBeanConstructorResultTransformer(
                                ItemSummary.class.getConstructor(
                                        Long.class,
                                        String.class,
                                        Date.class
                                )
                        )
                );

                List<ItemSummary> result = query.list();
                assertNotNull(result.get(0));
                assertEquals(result.size(), 3);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2"})
    public void executeRecursiveQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Session session = em.unwrap(Session.class);
            {
                org.hibernate.Query query = session.getNamedQuery("findAllCategoriesHibernate");

                List<Object[]> result = query.list();

                for (Object[] tuple : result) {
                    Category category = (Category) tuple[0];
                    String path = (String) tuple[1];
                    Integer level = (Integer) tuple[2];
                    // 做其他事儿
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
