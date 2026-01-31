package study.ywork.jpa.filtering;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.filtering.cascade.BankAccount;
import study.ywork.jpa.model.filtering.cascade.Bid;
import study.ywork.jpa.model.filtering.cascade.BillingDetails;
import study.ywork.jpa.model.filtering.cascade.CreditCard;
import study.ywork.jpa.model.filtering.cascade.Item;
import study.ywork.jpa.model.filtering.cascade.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CascadeTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("filteringCascade");
    }

    @Test
    public void detachMerge() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Long itemId;
            {
                User user = new User("tester");
                em.persist(user);

                Item item = new Item("Some Item", user);
                em.persist(item);
                itemId = item.getId();

                Bid firstBid = new Bid(new BigDecimal("99.00"), item);
                item.getBids().add(firstBid);
                em.persist(firstBid);

                Bid secondBid = new Bid(new BigDecimal("100.00"), item);
                item.getBids().add(secondBid);
                em.persist(secondBid);

                em.flush();
            }
            em.clear();

            Item item = em.find(Item.class, itemId);
            assertEquals(item.getBids().size(), 2);
            em.detach(item);

            em.clear();

            item.setName("New Name");

            Bid bid = new Bid(new BigDecimal("101.00"), item);
            item.getBids().add(bid);
            Item mergedItem = em.merge(item);

            for (Bid b : mergedItem.getBids()) {
                assertNotNull(b.getId());
            }

            em.flush();
            em.clear();

            item = em.find(Item.class, itemId);
            assertEquals(item.getName(), "New Name");
            assertEquals(item.getBids().size(), 3);

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void refresh() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Long userId;
            Long creditCardId = null;
            {

                User user = new User("tester");
                user.getBillingDetails().add(
                        new CreditCard("John Doe", "1234567890", "11", "2020")
                );
                user.getBillingDetails().add(
                        new BankAccount("John Doe", "45678", "Some Bank", "1234")
                );
                em.persist(user);
                em.flush();

                userId = user.getId();
                for (BillingDetails bd : user.getBillingDetails()) {
                    if (bd instanceof CreditCard)
                        creditCardId = bd.getId();
                }
                assertNotNull(creditCardId);
            }
            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            User user = em.find(User.class, userId);
            assertEquals(user.getBillingDetails().size(), 2);

            for (BillingDetails bd : user.getBillingDetails()) {
                assertEquals(bd.getOwner(), "John Doe");
            }

            final Long someUserId = userId;
            Executors.newSingleThreadExecutor().submit(() -> {
                UserTransaction tx1 = tm.getUserTransaction();

                try {
                    tx1.begin();
                    EntityManager em1 = jpa.createEntityManager();

                    em1.unwrap(Session.class).doWork(con -> {
                        PreparedStatement ps;
                        ps = con.prepareStatement(
                                "update BILLINGDETAILS set OWNER = ? where USER_ID = ?"
                        );
                        ps.setString(1, "Doe John");
                        ps.setLong(2, someUserId);
                        ps.executeUpdate();
                    });

                    tx1.commit();
                    em1.close();
                } catch (Exception ex) {
                    tm.rollback();
                }
                return null;
            }).get();
            em.refresh(user);

            for (BillingDetails bd : user.getBillingDetails()) {
                assertEquals(bd.getOwner(), "Doe John");
            }

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void replicate() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            Long itemId;

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                User user = new User("tester");
                em.persist(user);

                Item item = new Item("Some Item", user);
                em.persist(item);
                itemId = item.getId();

                tx.commit();
                em.close();
            }

            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            assertNotNull(item.getSeller().getUsername());

            tx.commit();
            em.close();

            tx.begin();
            EntityManager otherDatabase = jpa.createEntityManager();

            otherDatabase.unwrap(Session.class).replicate(item, ReplicationMode.OVERWRITE);

            tx.commit();
            otherDatabase.close();

        } finally {
            tm.rollback();
        }
    }
}
