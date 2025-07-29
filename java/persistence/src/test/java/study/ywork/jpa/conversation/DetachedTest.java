package study.ywork.jpa.conversation;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.conversation.Image;
import study.ywork.jpa.model.conversation.Item;
import study.ywork.jpa.model.conversation.User;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DetachedTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("conversation");
    }

    @Test
    public void businessKeyEquality() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            User user = new User("tester");
            em.persist(user);
            tx.commit();
            em.close();

            Long USER_ID = user.getId();

            tx.begin();
            em = jpa.createEntityManager();

            User a = em.find(User.class, USER_ID);
            User b = em.find(User.class, USER_ID);
            assertTrue(a == b);
            assertTrue(a.equals(b));
            assertEquals(a.getId(), b.getId());

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            User c = em.find(User.class, USER_ID);
            assertFalse(a == c);
            assertTrue(a.equals(c));
            assertEquals(a.getId(), c.getId());

            tx.commit();
            em.close();

            Set<User> allUsers = new HashSet();
            allUsers.add(a);
            allUsers.add(b);
            allUsers.add(c);
            assertEquals(allUsers.size(), 1);

        } finally {
            tm.rollback();
        }
    }

    public TestData storeItemImagesTestData() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();
        Long[] ids = new Long[1];
        Item item = new Item();
        item.setName("Some Item");
        em.persist(item);
        ids[0] = item.getId();
        for (int i = 1; i <= 3; i++) {
            item.getImages().add(
                    new Image("Image " + i, "image" + i + ".jpg", 640, 480));
        }
        tx.commit();
        em.close();
        return new TestData(ids);
    }
}
