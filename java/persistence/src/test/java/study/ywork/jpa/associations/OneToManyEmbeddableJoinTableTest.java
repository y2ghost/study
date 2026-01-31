package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetomany.embeddablejointable.Address;
import study.ywork.jpa.model.associations.onetomany.embeddablejointable.Shipment;
import study.ywork.jpa.model.associations.onetomany.embeddablejointable.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class OneToManyEmbeddableJoinTableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToManyEmbeddableJoinTable");
    }

    @Test
    public void storeAndLoadUsersShipments() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            User user = new User("tester");
            Address deliveryAddress = new Address("Some Street", "12345", "Some City");
            user.setShippingAddress(deliveryAddress);
            em.persist(user);

            Shipment firstShipment = new Shipment();
            deliveryAddress.getDeliveries().add(firstShipment);
            em.persist(firstShipment);

            Shipment secondShipment = new Shipment();
            deliveryAddress.getDeliveries().add(secondShipment);
            em.persist(secondShipment);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Long userId = user.getId();

            User tester = em.find(User.class, userId);
            assertEquals(tester.getShippingAddress().getDeliveries().size(), 2);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}