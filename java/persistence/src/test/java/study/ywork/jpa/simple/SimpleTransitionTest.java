package study.ywork.jpa.simple;

import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.simple.Address;
import study.ywork.jpa.model.simple.Item;
import study.ywork.jpa.model.simple.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SimpleTransitionTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simple");
    }

    @Test
    public void basicUOW() {
        EntityManager em = null;
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            em = jpa.createEntityManager();

            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);

            tx.commit();
        } catch (Exception ex) {
            try {
                if (tx.getStatus() == Status.STATUS_ACTIVE
                        || tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                    tx.rollback();
            } catch (Exception rbEx) {
                System.err.println("Rollback of transaction failed, trace follows!");
                rbEx.printStackTrace(System.err);
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Test
    public void makePersistent() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            EntityManager em;

            tx.begin();
            em = jpa.createEntityManager();
            Item item = new Item();
            item.setName("Some Item");

            em.persist(item);

            Long itemId = item.getId();

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            assertEquals(em.find(Item.class, itemId).getName(), "Some Item");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void retrievePersistent() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            long itemId = someItem.getId();

            {
                tx.begin();
                em = jpa.createEntityManager();
                Item item = em.find(Item.class, itemId);

                if (item != null) {
                    item.setName("New Name");
                }

                tx.commit();
                em.close();
            }

            {
                tx.begin();
                em = jpa.createEntityManager();

                Item itemA = em.find(Item.class, itemId);
                Item itemB = em.find(Item.class, itemId);

                assertTrue(itemA == itemB);
                assertTrue(itemA.equals(itemB));
                assertTrue(itemA.getId().equals(itemB.getId()));

                tx.commit();
                em.close();
            }

            tx.begin();
            em = jpa.createEntityManager();
            assertEquals(em.find(Item.class, itemId).getName(), "New Name");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.LazyInitializationException.class)
    public void retrievePersistentReference() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.getReference(Item.class, itemId);
            PersistenceUnitUtil persistenceUtil =
                    jpa.getEntityManagerFactory().getPersistenceUnitUtil();
            assertFalse(persistenceUtil.isLoaded(item));
            tx.commit();
            em.close();
            assertEquals(item.getName(), "Some Item");
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void makeTransient() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            em.remove(item);
            assertFalse(em.contains(item));
            assertNull(item.getId());

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            item = em.find(Item.class, itemId);
            assertNull(item);
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void refresh() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            final long ITEM_ID = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, ITEM_ID);
            item.setName("Some Name");

            Executors.newSingleThreadExecutor().submit(new Callable<>() {
                @Override
                public Object call() throws Exception {
                    UserTransaction tx = tm.getUserTransaction();
                    try {
                        tx.begin();
                        EntityManager em = jpa.createEntityManager();

                        Session session = em.unwrap(Session.class);
                        session.doWork(con -> {
                            PreparedStatement ps = con.prepareStatement("update ITEM set name = ? where ID = ?");
                            ps.setString(1, "Concurrent Update Name");
                            ps.setLong(2, ITEM_ID);

                            if (ps.executeUpdate() != 1) {
                                throw new SQLException("ITEM row was not updated");
                            }
                        });

                        tx.commit();
                        em.close();

                    } catch (Exception ex) {
                        tm.rollback();
                        throw new RuntimeException("Concurrent operation failure: " + ex, ex);
                    }
                    return null;
                }
            }).get();

            String oldName = item.getName();
            em.refresh(item);
            assertNotEquals(item.getName(), oldName);
            assertEquals(item.getName(), "Concurrent Update Name");

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2", "POSTGRESQL"})
    public void replicate() throws Exception {
        Long itemId;
        try {
            UserTransaction tx = tm.getUserTransaction();
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            itemId = someItem.getId();
        } finally {
            tm.rollback();
        }

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();

            EntityManager emA = getDatabaseA().createEntityManager();
            Item item = emA.find(Item.class, itemId);

            EntityManager emB = getDatabaseB().createEntityManager();
            emB.unwrap(Session.class)
                    .replicate(item, org.hibernate.ReplicationMode.LATEST_VERSION);

            tx.commit();
            emA.close();
            emB.close();
        } finally {
            tm.rollback();
        }
    }

    protected EntityManagerFactory getDatabaseA() {
        return jpa.getEntityManagerFactory();
    }

    protected EntityManagerFactory getDatabaseB() {
        return jpa.getEntityManagerFactory();
    }

    @Test
    public void flushModeType() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        Long itemId;
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Original Name");
            em.persist(someItem);
            tx.commit();
            em.close();
            itemId = someItem.getId();
        } finally {
            tm.rollback();
        }

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            item.setName("New Name");
            em.setFlushMode(FlushModeType.COMMIT);

            assertEquals(
                    em.createQuery("select i.name from Item i where i.id = :id")
                            .setParameter("id", itemId).getSingleResult(),
                    "Original Name"
            );

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void scopeOfIdentity() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some Item");
            em.persist(someItem);
            tx.commit();
            em.close();
            long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item a = em.find(Item.class, itemId);
            Item b = em.find(Item.class, itemId);
            assertTrue(a == b);
            assertTrue(a.equals(b));
            assertEquals(a.getId(), b.getId());

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Item c = em.find(Item.class, itemId);
            assertTrue(a != c);
            assertFalse(a.equals(c));
            assertEquals(a.getId(), c.getId());

            tx.commit();
            em.close();

            Set<Item> allItems = new HashSet<>();
            allItems.add(a);
            allItems.add(b);
            allItems.add(c);
            assertEquals(allItems.size(), 2);

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void detach() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User someUser = new User();
            someUser.setUsername("tester");
            someUser.setHomeAddress(new Address("Some Street", "1234", "Some City"));
            em.persist(someUser);
            tx.commit();
            em.close();
            long userId = someUser.getId();

            tx.begin();
            em = jpa.createEntityManager();

            User user = em.find(User.class, userId);

            em.detach(user);

            assertFalse(em.contains(user));

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void mergeDetached() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User detachedUser = new User();
            detachedUser.setUsername("foo");
            detachedUser.setHomeAddress(new Address("Some Street", "1234", "Some City"));
            em.persist(detachedUser);
            tx.commit();
            em.close();
            long userId = detachedUser.getId();

            detachedUser.setUsername("tester");

            tx.begin();
            em = jpa.createEntityManager();

            User mergedUser = em.merge(detachedUser);
            mergedUser.setUsername("doejohn");

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();
            User user = em.find(User.class, userId);
            assertEquals(user.getUsername(), "doejohn");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
