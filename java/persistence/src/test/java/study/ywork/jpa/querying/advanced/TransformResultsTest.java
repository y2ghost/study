package study.ywork.jpa.querying.advanced;

import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.ToListResultTransformer;
import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.ItemSummary;
import study.ywork.jpa.model.querying.ItemSummaryFactory;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class TransformResultsTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Session session = em.unwrap(Session.class);
            org.hibernate.Query query = session.createQuery(
                    "select i.id as itemId, i.name as name, i.auctionEnd as auctionEnd from Item i"
            );

            {
                List<Object[]> result = query.list();

                for (Object[] tuple : result) {
                    Long itemId = (Long) tuple[0];
                    String name = (String) tuple[1];
                    Date auctionEnd = (Date) tuple[2];
                    // NOOP
                }
                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                query.setResultTransformer(
                        ToListResultTransformer.INSTANCE
                );

                List<List> result = query.list();

                for (List list : result) {
                    Long itemId = (Long) list.get(0);
                    String name = (String) list.get(1);
                    Date auctionEnd = (Date) list.get(2);
                    // 做其他事儿
                }
                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                query.setResultTransformer(
                        AliasToEntityMapResultTransformer.INSTANCE
                );

                List<Map> result = query.list();


                assertEquals(
                        query.getReturnAliases(),
                        new String[]{"itemId", "name", "auctionEnd"}
                );

                for (Map map : result) {
                    Long itemId = (Long) map.get("itemId");
                    String name = (String) map.get("name");
                    Date auctionEnd = (Date) map.get("auctionEnd");
                    // 做其他事儿
                }
                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                org.hibernate.Query entityQuery = session.createQuery(
                        "select i as item, u as seller from Item i join i.seller u"
                );

                entityQuery.setResultTransformer(
                        AliasToEntityMapResultTransformer.INSTANCE
                );

                List<Map> result = entityQuery.list();

                for (Map map : result) {
                    Item item = (Item) map.get("item");
                    User seller = (User) map.get("seller");

                    assertEquals(item.getSeller(), seller);
                }
                assertEquals(result.size(), 3);
            }
            em.clear();

            {
                query.setResultTransformer(
                        new AliasToBeanResultTransformer(ItemSummary.class)
                );

                List<ItemSummary> result = query.list();

                for (ItemSummary itemSummary : result) {
                    Long itemId = itemSummary.getItemId();
                    String name = itemSummary.getName();
                    Date auctionEnd = itemSummary.getAuctionEnd();
                    // 做其他事儿
                }
                assertEquals(result.size(), 3);
            }
            em.clear();


            {
                query.setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] tuple, String[] aliases) {

                                Long itemId = (Long) tuple[0];
                                String name = (String) tuple[1];
                                Date auctionEnd = (Date) tuple[2];
                                assertEquals(aliases[0], "itemId");
                                assertEquals(aliases[1], "name");
                                assertEquals(aliases[2], "auctionEnd");

                                return ItemSummaryFactory.newItemSummary(
                                        itemId, name, auctionEnd
                                );
                            }

                            @Override
                            public List transformList(List collection) {
                                // The "collection" is a List<ItemSummary>
                                return Collections.unmodifiableList(collection);
                            }
                        }
                );

                List<ItemSummary> result = query.list();
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
