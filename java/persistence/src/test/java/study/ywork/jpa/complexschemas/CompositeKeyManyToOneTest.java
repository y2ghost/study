package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.compositekey.manytoone.Item;
import study.ywork.jpa.model.complexschemas.compositekey.manytoone.User;
import study.ywork.jpa.model.complexschemas.compositekey.manytoone.UserId;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class CompositeKeyManyToOneTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("compositeKeyManyToOne");
    }

    @Test
    public void storeLoad() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                UserId id = new UserId("tester", "123");
                User user = new User(id);
                em.persist(user);

                Item item = new Item("Some Item");
                item.setSeller(user);
                em.persist(item);
            }

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            {
                UserId id = new UserId("tester", "123");
                User user = em.find(User.class, id);
                assertEquals(user.getId().getDepartmentNr(), "123");

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
