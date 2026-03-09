package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.naturalforeignkey.Item;
import study.ywork.jpa.model.complexschemas.naturalforeignkey.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class NaturalForeignKeyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("naturalForeignKey");
    }

    @Test
    public void storeLoad() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Long userId;
            {
                User user = new User("1234");
                em.persist(user);

                Item item = new Item("Some Item");
                item.setSeller(user);
                em.persist(item);
                userId = user.getId();
            }

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            {
                User user = em.find(User.class, userId);

                Item item = (Item) em.createQuery(
                        "select i from Item i where i.seller = :u"
                ).setParameter("u", user).getSingleResult();

                assertEquals(item.getName(), "Some Item");
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
