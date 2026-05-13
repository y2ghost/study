package study.ywork.jpa.inheritance;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.inheritance.embeddable.Dimensions;
import study.ywork.jpa.model.inheritance.embeddable.Item;
import study.ywork.jpa.model.inheritance.embeddable.Weight;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class EmbeddableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("inheritanceEmbeddable");
    }

    @Test
    public void storeAndLoad() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item item1 = new Item(
                    "JUnit in Action, Second Edition",
                    Dimensions.centimeters(
                            BigDecimal.valueOf(19.0d),
                            BigDecimal.valueOf(23.5d),
                            BigDecimal.valueOf(3.0d)),
                    Weight.kilograms(BigDecimal.valueOf(4.0d)));
            em.persist(item1);

            Item item2 = new Item(
                    "Java Persistence with Hibernate. Second Edition",
                    Dimensions.inches(
                            BigDecimal.valueOf(7.5d),
                            BigDecimal.valueOf(9.25d),
                            BigDecimal.valueOf(2.0d)),
                    Weight.pounds(BigDecimal.valueOf(3.0d)));
            em.persist(item2);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, item1.getId());
            assertEquals(item.getName(), "JUnit in Action, Second Edition");
            assertEquals(item.getDimensions().getName(), "centimeters");
            assertEquals(item.getDimensions().getSymbol(), "cm");
            assertEquals(item.getDimensions().getWidth().compareTo(new BigDecimal("19")), 0);
            assertEquals(item.getDimensions().getHeight().compareTo(new BigDecimal("23.5")), 0);
            assertEquals(item.getDimensions().getDepth().compareTo(new BigDecimal("3")), 0);
            assertEquals(item.getWeight().getValue().compareTo(new BigDecimal("4")), 0);
            assertEquals(item.getWeight().getName(), "kilograms");
            assertEquals(item.getWeight().getSymbol(), "kg");

            item = em.find(Item.class, item2.getId());
            assertEquals(item.getName(), "Java Persistence with Hibernate. Second Edition");
            assertEquals(item.getDimensions().getName(), "inches");
            assertEquals(item.getDimensions().getSymbol(), "\"");
            assertEquals(item.getDimensions().getWidth().compareTo(new BigDecimal("7.5")), 0);
            assertEquals(item.getDimensions().getHeight().compareTo(new BigDecimal("9.25")), 0);
            assertEquals(item.getDimensions().getDepth().compareTo(new BigDecimal("2")), 0);
            assertEquals(item.getWeight().getValue().compareTo(new BigDecimal("3")), 0);
            assertEquals(item.getWeight().getName(), "pounds");
            assertEquals(item.getWeight().getSymbol(), "lbs");

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}
