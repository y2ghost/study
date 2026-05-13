package study.ywork.jpa.simple;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.simple.Address;
import study.ywork.jpa.model.simple.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class MappingEmbeddableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simple");
    }

    @Test
    public void storeAndLoadUsers() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setUsername("tester");
            Address homeAddress = new Address("Some Street 123", "12345", "Some City");
            user.setHomeAddress(homeAddress);
            em.persist(user);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            User u = em.find(User.class, user.getId());

            assertEquals(u.getUsername(), "tester");
            assertEquals(u.getHomeAddress().getStreet(), "Some Street 123");

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeAndLoadInvalidUsers() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setUsername("tester");
            Address homeAddress = new Address("Some Street 123", "12345", null); // NULL city!
            user.setHomeAddress(homeAddress);
            em.persist(user);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }
}
