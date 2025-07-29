package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.compositekey.readonly.Department;
import study.ywork.jpa.model.complexschemas.compositekey.readonly.User;
import study.ywork.jpa.model.complexschemas.compositekey.readonly.UserId;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class CompositeKeyReadOnlyTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("compositeKeyReadOnly");
    }

    @Test
    public void storeLoad() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Long departmentId;
            {
                Department department = new Department("Sales");
                em.persist(department);

                UserId id = new UserId("tester", department.getId());
                User user = new User(id);
                em.persist(user);

                assertNull(user.getDepartment());

                departmentId = department.getId();
            }

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            {
                UserId id = new UserId("tester", departmentId);
                User user = em.find(User.class, id);
                assertEquals(user.getDepartment().getName(), "Sales");
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
