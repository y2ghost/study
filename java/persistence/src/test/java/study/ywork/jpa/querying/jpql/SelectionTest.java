package study.ywork.jpa.querying.jpql;

import org.testng.annotations.Test;
import study.ywork.jpa.model.inheritance.tableperclass.BankAccount;
import study.ywork.jpa.model.inheritance.tableperclass.CreditCard;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SelectionTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            { // This only works in Hibernate, SELECT clause isn't optional in JPA
                Query q = em.createNamedQuery("selection");
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("aliases");
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("aliasesAs");
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("polymorphismObject");
                assertEquals(q.getResultList().size(), 18);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("polymorphismInterface");
                assertEquals(q.getResultList().size(), 3);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("polymorphismBillingDetails");
                assertEquals(q.getResultList().size(), 2);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("polymorphismCreditCard");
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("polymorphismRestrictName");
                assertEquals(q.getResultList().size(), 1);
            }
            em.clear();
            {
                Query query = // ...
                        em.createNamedQuery("polymorphismRestrictParameter");
                query.setParameter("types", Arrays.asList(CreditCard.class, BankAccount.class));
                assertEquals(query.getResultList().size(), 2);
            }
            em.clear();
            {
                Query q = em.createNamedQuery("polymorphismRestrictNot");
                assertEquals(q.getResultList().size(), 1);
                assertTrue(q.getResultList().iterator().next() instanceof CreditCard);
            }
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
