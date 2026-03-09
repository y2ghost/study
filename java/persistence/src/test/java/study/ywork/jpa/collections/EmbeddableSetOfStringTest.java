package study.ywork.jpa.collections;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.collections.embeddablesetofstrings.Address;
import study.ywork.jpa.model.collections.embeddablesetofstrings.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class EmbeddableSetOfStringTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("embeddableSetOfString");
    }

    @Test
    public void storeLoadCollection() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User("tester");
            Address address = new Address("Some Street", "1234", "Some City");
            user.setAddress(address);

            address.getContacts().add("Foo");
            address.getContacts().add("Bar");
            address.getContacts().add("Baz");

            em.persist(user);
            tx.commit();
            em.close();

            Long userId = user.getId();

            tx.begin();
            em = jpa.createEntityManager();

            User tester = em.find(User.class, userId);
            assertEquals(tester.getAddress().getContacts().size(), 3);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
