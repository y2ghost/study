package study.ywork.jpa.converter;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.MonetaryAmount;
import study.ywork.jpa.model.advanced.usertype.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class UserTypeTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("userType");
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

            item.setBuyNowPrice(
                    new MonetaryAmount(
                            new BigDecimal("456"), Currency.getInstance("CHF")
                    )
            );

            item.setInitialPrice(
                    new MonetaryAmount(
                            new BigDecimal("123"), Currency.getInstance("GBP")
                    )
            );

            em.persist(item);
            tx.commit();
            em.close();

            Long itemId = item.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item i = em.find(Item.class, itemId);
            assertEquals(i.getBuyNowPrice().getValue().compareTo(new BigDecimal("912")), 0);
            assertEquals(i.getBuyNowPrice().getCurrency(), Currency.getInstance("USD"));
            assertEquals(i.getInitialPrice().getValue().compareTo(new BigDecimal("246")), 0);
            assertEquals(i.getInitialPrice().getCurrency(), Currency.getInstance("EUR"));

            List<Double> averagePrice = em.createQuery(
                    "select avg(i.initialPrice.value) from Item i"
            ).getResultList();
            assertEquals(averagePrice.get(0), 246.0d);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
