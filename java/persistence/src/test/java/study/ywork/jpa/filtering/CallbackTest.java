package study.ywork.jpa.filtering;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.filtering.callback.CurrentUser;
import study.ywork.jpa.model.filtering.callback.Item;
import study.ywork.jpa.model.filtering.callback.Mail;
import study.ywork.jpa.model.filtering.callback.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CallbackTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("filteringCallback");
    }

    @Test
    public void notifyPostPersist() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                User user = new User("tester");
                CurrentUser.INSTANCE.set(user);

                em.persist(user);
                assertEquals(Mail.INSTANCE.size(), 0);
                em.flush();
                assertEquals(Mail.INSTANCE.size(), 1);
                assertTrue(Mail.INSTANCE.get(0).contains("tester"));
                Mail.INSTANCE.clear();


                Item item = new Item("Foo", user);
                em.persist(item);
                assertEquals(Mail.INSTANCE.size(), 0);
                em.flush();
                assertEquals(Mail.INSTANCE.size(), 1);
                assertTrue(Mail.INSTANCE.get(0).contains("tester"));
                Mail.INSTANCE.clear();

                CurrentUser.INSTANCE.set(null);
            }
            em.clear();

        } finally {
            tm.rollback();
        }
    }
}
