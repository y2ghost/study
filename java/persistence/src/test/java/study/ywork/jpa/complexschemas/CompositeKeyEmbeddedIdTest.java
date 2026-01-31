package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.compositekey.embedded.User;
import study.ywork.jpa.model.complexschemas.compositekey.embedded.UserId;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class CompositeKeyEmbeddedIdTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("compositeKeyEmbeddedId");
    }

    @Test
    public void storeLoad() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                UserId id = new UserId("tester", "123");
                User user = new User(id);
                em.persist(user);
            }

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            {
                UserId id = new UserId("tester", "123");
                User user = em.find(User.class, id);
                assertEquals(user.getId().getDepartmentNr(), "123");
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
