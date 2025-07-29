package study.ywork.jpa.complexschemas;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.complexschemas.compositekey.mapsid.Department;
import study.ywork.jpa.model.complexschemas.compositekey.mapsid.User;
import study.ywork.jpa.model.complexschemas.compositekey.mapsid.UserId;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class CompositeKeyMapsIdTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("compositeKeyMapsId");
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

                UserId id = new UserId("tester", null);
                User user = new User(id);
                user.setDepartment(department);
                em.persist(user);

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
