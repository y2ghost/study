package study.ywork.jpa.hello;


import org.testng.annotations.Test;
import study.ywork.jpa.env.TransactionManager;
import study.ywork.jpa.model.hello.Message;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class HelloJPATest extends TransactionManager {
    @Test
    @SuppressWarnings("unchecked")
    public void storeLoadMessage() throws Exception {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
            {
                UserTransaction tx = tm.getUserTransaction();
                tx.begin();
                EntityManager em = emf.createEntityManager();
                Message message = new Message();
                message.setText("Hello World!");
                em.persist(message);
                tx.commit();
                em.close();
            }

            {
                UserTransaction tx = tm.getUserTransaction();
                tx.begin();
                EntityManager em = emf.createEntityManager();
                List<Message> messages = em.createQuery("select m from Message m").getResultList();
                assertEquals(messages.size(), 1);
                assertEquals(messages.get(0).getText(), "Hello World!");
                messages.get(0).setText("Take me to your leader!");
                tx.commit();
                em.close();
            }
        } finally {
            tm.rollback();
        }
    }
}
