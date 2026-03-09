package study.ywork.jpa.advanced;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Address;
import study.ywork.jpa.model.advanced.City;
import study.ywork.jpa.model.advanced.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Locale;

import static org.testng.Assert.assertEquals;

public class NestedComponentTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }

    @Test
    public void storeAndLoadUsers() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            City city = new City();
            city.setZipcode("12345");
            city.setName("Some City");
            city.setCountry(Locale.GERMANY.getCountry());

            Address address = new Address();
            address.setStreet("Some Street 123");
            address.setCity(city);

            User userOne = new User();
            userOne.setAddress(address);

            em.persist(userOne);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            User u = em.find(User.class, userOne.getId());

            assertEquals(u.getAddress().getStreet(), "Some Street 123");
            assertEquals(u.getAddress().getCity().getZipcode(), "12345");
            assertEquals(u.getAddress().getCity().getCountry(), Locale.GERMANY.getCountry());

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
