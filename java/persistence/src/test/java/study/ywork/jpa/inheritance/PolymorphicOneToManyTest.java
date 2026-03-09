package study.ywork.jpa.inheritance;


import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.inheritance.associations.onetomany.BankAccount;
import study.ywork.jpa.model.inheritance.associations.onetomany.BillingDetails;
import study.ywork.jpa.model.inheritance.associations.onetomany.CreditCard;
import study.ywork.jpa.model.inheritance.associations.onetomany.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class PolymorphicOneToManyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("polymorphicOneToMany");
    }

    @Test
    public void storeAndLoadItemBids() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            BankAccount ba = new BankAccount(
                    "Jane Roe", "445566", "One Percent Bank Inc.", "999"
            );
            CreditCard cc = new CreditCard(
                    "John Doe", "1234123412341234", "06", "2015"
            );
            User tester = new User("tester");

            tester.getBillingDetails().add(ba);
            ba.setUser(tester);

            tester.getBillingDetails().add(cc);
            cc.setUser(tester);

            em.persist(ba);
            em.persist(cc);
            em.persist(tester);

            tx.commit();
            em.close();

            Long userId = tester.getId();

            tx.begin();
            em = jpa.createEntityManager();
            {
                User user = em.find(User.class, userId);

                for (BillingDetails billingDetails : user.getBillingDetails()) {
                    billingDetails.pay(123);
                }
                assertEquals(user.getBillingDetails().size(), 2);
            }

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}