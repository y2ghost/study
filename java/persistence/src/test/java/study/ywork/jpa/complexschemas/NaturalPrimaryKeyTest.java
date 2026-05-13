package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.naturalprimarykey.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertNotNull;

public class NaturalPrimaryKeyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("naturalPrimaryKey");
    }

    @Test
    public void storeLoad() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            {
                User user = new User("tester");
                em.persist(user);
            }
            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            {
                User user = em.find(User.class, "tester");
                assertNotNull(user);
            }
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
