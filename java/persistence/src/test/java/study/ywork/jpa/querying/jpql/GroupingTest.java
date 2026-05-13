package study.ywork.jpa.querying.jpql;

import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GroupingTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Query q = em.createNamedQuery("group");
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 2);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof String);
                    assertTrue(row[1] instanceof Long);
                }
            }
            em.clear();
            {
                Query q = em.createNamedQuery("average");
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 2);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof String);
                    assertTrue(row[1] instanceof Double);
                }
            }
            em.clear();
            {
                Query q = em.createNamedQuery("averageWorkaround");
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 2);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof Item);
                    assertTrue(row[1] instanceof Double);
                }
            }
            em.clear();
            {
                Query q = em.createNamedQuery("having");
                List<Object[]> result = q.getResultList();
                assertEquals(result.size(), 1);
                for (Object[] row : result) {
                    assertTrue(row[0] instanceof String);
                    assertTrue(row[1] instanceof Long);
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
