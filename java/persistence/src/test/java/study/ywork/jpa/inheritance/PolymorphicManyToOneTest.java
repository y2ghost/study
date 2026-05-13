package study.ywork.jpa.inheritance;


import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.inheritance.associations.manytoone.BillingDetails;
import study.ywork.jpa.model.inheritance.associations.manytoone.CreditCard;
import study.ywork.jpa.model.inheritance.associations.manytoone.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;

public class PolymorphicManyToOneTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("polymorphicManyToOne");
    }

    @Test
    public void storeAndLoadItemBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            CreditCard cc = new CreditCard(
                    "John Doe", "1234123412341234", "06", "2015"
            );
            User tester = new User("tester");
            tester.setDefaultBilling(cc);

            em.persist(cc);
            em.persist(tester);

            tx.commit();
            em.close();

            Long userId = tester.getId();
            tx.begin();
            em = jpa.createEntityManager();

            {
                User user = em.find(User.class, userId);
                user.getDefaultBilling().pay(123);
                assertEquals(user.getDefaultBilling().getOwner(), "John Doe");
            }

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            {
                User user = em.find(User.class, userId);
                BillingDetails bd = user.getDefaultBilling();
                assertFalse(bd instanceof CreditCard);
            }
            {
                User user = em.find(User.class, userId);
                BillingDetails bd = user.getDefaultBilling();
                CreditCard creditCard = em.getReference(CreditCard.class, bd.getId());
                assertNotSame(bd, creditCard);
            }
            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            {
                User user = (User) em.createQuery(
                                "select u from User u " +
                                        "left join fetch u.defaultBilling " +
                                        "where u.id = :id")
                        .setParameter("id", userId)
                        .getSingleResult();
                CreditCard creditCard = (CreditCard) user.getDefaultBilling();
                assertNotNull(creditCard);
            }
            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}