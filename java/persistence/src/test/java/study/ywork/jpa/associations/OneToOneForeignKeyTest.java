package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetoone.foreignkey.Address;
import study.ywork.jpa.model.associations.onetoone.foreignkey.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class OneToOneForeignKeyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToOneForeignKey");
    }

    @Test
    public void storeAndLoadUserAddress() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User someUser =
                    new User("tester");

            Address someAddress =
                    new Address("Some Street 123", "12345", "Some City");

            someUser.setShippingAddress(someAddress);

            em.persist(someUser);

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

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeNonUniqueRelationship() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Address someAddress = new Address("Some Street 123", "12345", "Some City");

            User userOne = new User("tester");
            userOne.setShippingAddress(someAddress);
            em.persist(userOne);

            User userTwo = new User("dev");
            userTwo.setShippingAddress(someAddress);
            em.persist(userTwo);

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