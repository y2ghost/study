package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetoone.sharedprimarykey.Address;
import study.ywork.jpa.model.associations.onetoone.sharedprimarykey.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class OneToOneSharedPrimaryKeyTest extends JpaManager {

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToOneSharedPrimaryKey");
    }

    @Test
    public void storeAndLoadUserAddress() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Address someAddress =
                    new Address("Some Street 123", "12345", "Some City");

            em.persist(someAddress);

            User someUser =
                    new User(
                            someAddress.getId(),
                            "tester"
                    );

            em.persist(someUser);

            someUser.setShippingAddress(someAddress);

            tx.commit();
            em.close();

            Long userId = someUser.getId();
            Long addressId = someAddress.getId();

            tx.begin();
            em = jpa.createEntityManager();

            User user = em.find(User.class, userId);
            assertEquals(user.getShippingAddress().getZipcode(), "12345");

            Address address = em.find(Address.class, addressId);
            assertEquals(address.getZipcode(), "12345");

            assertEquals(user.getId(), address.getId());

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}