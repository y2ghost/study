package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.custom.Bid;
import study.ywork.jpa.model.complexschemas.custom.Item;
import study.ywork.jpa.model.complexschemas.custom.User;
import study.ywork.jpa.share.util.CalendarUtil;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

@Test(groups = "H2")
public class CustomSchemaTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("customSchema");
    }

    @Test
    public void storeLoadDomainInvalid() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setEmail("@invalid.address");
            user.setUsername("someuser");
            em.persist(user);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeLoadCheckColumnInvalid() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setEmail("valid@test.com");
            user.setUsername("adminPretender");
            em.persist(user);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeLoadCheckSingleRowInvalid() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            // Wrong start/end time
            Item item = new Item("Some Item", CalendarUtil.TOMORROW.getTime(), CalendarUtil.TODAY.getTime());
            em.persist(item);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeLoadUniqueMultiColumnValid() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setEmail("valid@test.com");
            user.setUsername("someuser");
            em.persist(user);

            user = new User();
            user.setEmail("valid@test.com");
            user.setUsername("someuser");
            em.persist(user);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void storeLoadCheckSubselectValid() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {

            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item item = new Item("Some Item", CalendarUtil.TODAY.getTime(), CalendarUtil.TOMORROW.getTime());
            Bid bid = new Bid(new BigDecimal(1), item);
            bid.setCreatedOn(CalendarUtil.AFTER_TOMORROW.getTime()); // Out of date range of auction
            item.getBids().add(bid);

            em.persist(item);
            em.persist(bid);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void storeLoadValid() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User();
            user.setEmail("valid@test.com");
            user.setUsername("someuser");
            em.persist(user);

            user = new User();
            user.setEmail("valid2@test.com");
            user.setUsername("otheruser");
            em.persist(user);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            user = em.find(User.class, user.getId());
            assertEquals(user.getUsername(), "otheruser");
            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}
