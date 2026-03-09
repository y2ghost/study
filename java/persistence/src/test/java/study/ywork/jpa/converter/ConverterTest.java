package study.ywork.jpa.converter;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.MonetaryAmount;
import study.ywork.jpa.model.advanced.converter.Address;
import study.ywork.jpa.model.advanced.converter.GermanZipcode;
import study.ywork.jpa.model.advanced.converter.Item;
import study.ywork.jpa.model.advanced.converter.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Currency;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ConverterTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("converter");
    }

    @Test
    public void storeLoadMonetaryAmount() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            EntityManager em;

            tx.begin();
            em = jpa.createEntityManager();

            Item item = new Item();
            item.setName("Some Item");
            MonetaryAmount amount =
                    new MonetaryAmount(
                            new BigDecimal("11.23"), Currency.getInstance("USD")
                    );
            item.setBuyNowPrice(amount);
            em.persist(item);
            tx.commit();
            em.close();

            Long itemId = item.getId();

            tx.begin();
            em = jpa.createEntityManager();
            assertEquals(em.find(Item.class, itemId).getBuyNowPrice(), amount);
            assertEquals(em.find(Item.class, itemId).getBuyNowPrice().getValue(), new BigDecimal("11.23"));
            assertEquals(em.find(Item.class, itemId).getBuyNowPrice().getCurrency(), Currency.getInstance("USD"));
            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void storeAndLoadZipcode() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setUsername("tester");
            Address homeAddress =
                    new Address(
                            "Some Street 123",
                            new GermanZipcode("12345"),
                            "Some City"
                    );
            user.setHomeAddress(homeAddress);
            em.persist(user);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            User u = em.find(User.class, user.getId());

            assertEquals(u.getUsername(), "tester");
            assertTrue(u.getHomeAddress().getZipcode() instanceof GermanZipcode);
            assertEquals(u.getHomeAddress().getZipcode().getValue(), "12345");

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
