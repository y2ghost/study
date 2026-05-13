package study.ywork.jpa.customsql;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.customsql.procedures.User;
import study.ywork.jpa.share.FetchTestLoadEventListener;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class CRUDProceduresTest extends JpaManager {
    FetchTestLoadEventListener loadEventListener;

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("CRUDProcedures", "customsql/CRUDProcedures.hbm.xml");
    }

    @Override
    public void afterJPABootstrap() {
        loadEventListener = new FetchTestLoadEventListener(jpa.getEntityManagerFactory());
    }

    class CustomSQLTestData {
        TestData users;
    }

    public CustomSQLTestData create() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        CustomSQLTestData testData = new CustomSQLTestData();
        testData.users = new TestData(new Long[2]);

        User tester = new User("tester");
        em.persist(tester);
        testData.users.identifiers[0] = tester.getId();

        tx.commit();
        em.close();

        return testData;
    }

    @Test(groups = "MARIADB")
    public void read() throws Exception {
        CustomSQLTestData testData = create();
        Long USER_ID = testData.users.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                User user = em.find(User.class, USER_ID);
                assertEquals(loadEventListener.getLoadCount(User.class), 1);
                assertEquals(user.getId(), USER_ID);
            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void update() throws Exception {
        CustomSQLTestData testData = create();
        Long USER_ID = testData.users.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                User user = em.find(User.class, USER_ID);
                user.setUsername("jdoe");
                em.flush();

            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void delete() throws Exception {
        CustomSQLTestData testData = create();
        Long USER_ID = testData.users.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                User user = em.find(User.class, USER_ID);
                em.remove(user);
                em.flush();
                em.clear();

                assertNull(em.find(User.class, USER_ID));
            }
            em.clear();
            loadEventListener.reset();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
