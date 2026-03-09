package study.ywork.jpa.querying.criteria;

import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Address_;
import study.ywork.jpa.model.querying.Bid;
import study.ywork.jpa.model.querying.Bid_;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.model.querying.Item_;
import study.ywork.jpa.model.querying.User;
import study.ywork.jpa.model.querying.User_;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class TypesafeTest extends QueryingTest {
    @Test
    public void executeQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        CriteriaBuilder cb = jpa.getEntityManagerFactory().getCriteriaBuilder();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            {
                CriteriaQuery<User> criteria = cb.createQuery(User.class);
                Root<User> u = criteria.from(User.class);
                criteria.select(u).where(
                    cb.equal(
                        u.get(User_.homeAddress).get(Address_.city),
                        "Some City"));

                TypedQuery<User> q = em.createQuery(criteria);
                User user = q.getSingleResult();
                assertEquals(user.getId(), testData.users.getFirstId());
            }
            em.clear();
            {
                CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
                Root<Item> i = criteria.from(Item.class);
                Join<Item, Bid> b = i.join(Item_.bids);
                criteria.select(i).where(
                    cb.gt(b.get(Bid_.amount), new BigDecimal(100)));

                TypedQuery<Item> q = em.createQuery(criteria);
                List<Item> result = q.getResultList();
                assertEquals(result.size(), 1);
                assertEquals(result.get(0).getId(), testData.items.getFirstId());
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
