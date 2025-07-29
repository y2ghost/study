package study.ywork.jpa.advanced;

import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Bid;
import study.ywork.jpa.model.advanced.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TransformingColumnTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }

    @Test
    public void storeLoadTransform() throws Exception {
        final long ITEM_ID = storeItemAndBids();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Item item = em.find(Item.class, ITEM_ID);
                assertEquals(item.getMetricWeight(), 2.0);

                final boolean[] tests = new boolean[1];
                em.unwrap(Session.class).doWork(connection -> {
                    PreparedStatement statement = null;
                    ResultSet result = null;
                    try {
                        statement = connection.prepareStatement(
                                "select IMPERIALWEIGHT from ITEM where ID = ?"
                        );
                        statement.setLong(1, ITEM_ID);
                        result = statement.executeQuery();
                        while (result.next()) {
                            Double imperialWeight = result.getDouble("IMPERIALWEIGHT");
                            assertEquals(imperialWeight, 4.40924);
                            tests[0] = true;
                        }
                    } finally {
                        if (result != null)
                            result.close();
                        if (statement != null)
                            statement.close();
                    }
                });
                assertTrue(tests[0]);
            }
            em.clear();

            {
                List<Item> result =
                        em.createQuery("select i from Item i where i.metricWeight = :w")
                                .setParameter("w", 2.0)
                                .getResultList();
                assertEquals(result.size(), 1);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    public Long storeItemAndBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();
        Item item = new Item();
        item.setName("Some item");
        item.setMetricWeight(2);
        item.setDescription("This is some description.");
        em.persist(item);
        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid();
            bid.setAmount(new BigDecimal(10 + i));
            bid.setItem(item);
            em.persist(bid);
        }
        tx.commit();
        em.close();
        return item.getId();
    }
}
