package study.ywork.jpa.querying.jpql;

import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SubselectsTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            {
                Query q = em.createNamedQuery("correlated");
                List<User> result = q.getResultList();
                assertEquals(result.size(), 1);
                User user = result.iterator().next();
                assertEquals(user.getId(), testData.users.getFirstId());
            }
            em.clear();
            {
                Query q = em.createNamedQuery("uncorrelated");
                List<Bid> result = q.getResultList();
                assertEquals(result.size(), 2);
            }
            {
                Query q = em.createNamedQuery("exists");
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 2);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("quantifyAll");
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 2);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("quantifyAny");
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 1);
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
