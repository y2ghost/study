package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.secondarytable.Address;
import study.ywork.jpa.model.complexschemas.secondarytable.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class SecondaryTableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("secondaryTable");
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
}
