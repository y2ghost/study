package study.ywork.jpa.advanced;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class TemporalTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }


    @Test
    public void storeLoadTemporal() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some item");
            someItem.setDescription("This is some description.");
            em.persist(someItem);
            tx.commit();
            em.close();

            Long itemId = someItem.getId();
            Date originalCreationDate = someItem.getCreatedOn();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertFalse(item.getCreatedOn().equals(originalCreationDate));
            assertFalse(item.getCreatedOn().getClass().equals(originalCreationDate.getClass()));
            assertEquals(originalCreationDate.getTime(), item.getCreatedOn().getTime());

            Calendar oldDate = new GregorianCalendar();
            oldDate.setTime(originalCreationDate);
            Calendar newDate = new GregorianCalendar();
            newDate.setTime(item.getCreatedOn());
            assertEquals(oldDate, newDate);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
