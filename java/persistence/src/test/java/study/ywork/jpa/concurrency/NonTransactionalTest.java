package study.ywork.jpa.concurrency;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.concurrency.version.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class NonTransactionalTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("concurrencyVersioning");
    }

    @Test(groups = {"H2", "POSTGRESQL"})
    public void autoCommit() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        Long itemId;
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item("Original Name");
            em.persist(someItem);
            tx.commit();
            em.close();
            itemId = someItem.getId();
        } finally {
            tm.rollback();
        }

        {
            EntityManager em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            item.setName("New Name");
            assertEquals(
                    em.createQuery("select i.name from Item i where i.id = :id")
                            .setParameter("id", itemId).getSingleResult(),
                    "Original Name"
            );
            assertEquals(
                    ((Item) em.createQuery("select i from Item i where i.id = :id")
                            .setParameter("id", itemId).getSingleResult()).getName(),
                    "New Name"
            );
            em.refresh(item);
            assertEquals(item.getName(), "Original Name");

            em.close();
        }

        {
            EntityManager em = jpa.createEntityManager();
            Item newItem = new Item("New Item");
            em.persist(newItem);
            assertNotNull(newItem.getId());
            tx.begin();

            if (!em.isJoinedToTransaction()) {
                em.joinTransaction();
            }

            tx.commit();
            em.close();
        }

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            assertEquals(em.find(Item.class, itemId).getName(), "Original Name");
            assertEquals(em.createQuery("select count(i) from Item i").getSingleResult(), 2l);
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }

        {
            EntityManager tmp = jpa.createEntityManager();
            Item detachedItem = tmp.find(Item.class, itemId);
            tmp.close();

            detachedItem.setName("New Name");
            EntityManager em = jpa.createEntityManager();

            Item mergedItem = em.merge(detachedItem);

            tx.begin();
            em.joinTransaction();
            tx.commit();
            em.close();
        }

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            assertEquals(em.find(Item.class, itemId).getName(), "New Name");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }

        {
            EntityManager em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            em.remove(item);

            tx.begin();
            em.joinTransaction();
            tx.commit();
            em.close();
        }

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            assertEquals(em.createQuery("select count(i) from Item i").getSingleResult(), 1l);
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
