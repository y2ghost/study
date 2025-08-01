package study.ywork.jpa.querying.criteria;

import org.testng.annotations.Test;
import study.ywork.jpa.model.inheritance.tableperclass.BankAccount;
import study.ywork.jpa.model.inheritance.tableperclass.BillingDetails;
import study.ywork.jpa.model.inheritance.tableperclass.CreditCard;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.UserTransaction;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SelectionTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        storeTestData();
        CriteriaBuilder cb = jpa.getEntityManagerFactory().getCriteriaBuilder();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                CriteriaQuery criteria = cb.createQuery(Item.class);
                criteria.from(Item.class);

                List<Item> result = em.createQuery(criteria).getResultList();
                assertEquals(result.size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<Item> i = criteria.from(Item.class);
                criteria.select(i);

                List<Item> result = em.createQuery(criteria).getResultList();
                assertEquals(result.size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                criteria.select(criteria.from(Item.class));
                TypedQuery<Item> q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                EntityType entityType = getEntityType(
                    em.getMetamodel(), "Item"
                );

                criteria.select(criteria.from(entityType));
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                criteria.select(criteria.from(BillingDetails.class));
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                criteria.select(criteria.from(CreditCard.class));
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<BillingDetails> bd = criteria.from(BillingDetails.class);
                criteria.select(bd).where(
                    cb.equal(bd.type(), CreditCard.class)
                );
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<BillingDetails> bd = criteria.from(BillingDetails.class);
                criteria.select(bd).where(
                    cb.not(cb.equal(bd.type(), BankAccount.class))
                );
                Query q = em.createQuery(criteria);
                assertEquals(q.getResultList().size(), 1);
                assertTrue(q.getResultList().iterator().next() instanceof CreditCard);
            }
            em.clear();
            {
                CriteriaQuery criteria = cb.createQuery();
                Root<BillingDetails> bd = criteria.from(BillingDetails.class);
                criteria.select(bd).where(
                    bd.type().in(cb.parameter(List.class, "types"))
                );
                Query q = em.createQuery(criteria);
                q.setParameter("types", Arrays.asList(CreditCard.class, BankAccount.class));
                assertEquals(q.getResultList().size(), 2);
            }
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    protected EntityType getEntityType(Metamodel metamodel, String entityName) {
        EntityType entityType = null;
        for (EntityType<?> t : metamodel.getEntities()) {
            if (t.getName().equals(entityName)) {
                entityType = t;
                break;
            }
        }
        if (entityType == null)
            throw new IllegalStateException("Managed entity type not found for entity name: " + entityName);
        return entityType;
    }
}
