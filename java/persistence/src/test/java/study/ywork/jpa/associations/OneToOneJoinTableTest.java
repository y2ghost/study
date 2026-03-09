package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.onetoone.jointable.Item;
import study.ywork.jpa.model.associations.onetoone.jointable.Shipment;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class OneToOneJoinTableTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("oneToOneJoinTable");
    }

    @Test
    public void storeAndLoadUserAddress() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Shipment someShipment = new Shipment();
            em.persist(someShipment);

            Item someItem = new Item("Some Item");
            em.persist(someItem);

            Shipment auctionShipment = new Shipment(someItem);
            em.persist(auctionShipment);

            tx.commit();
            em.close();

            Long itemId = someItem.getId();
            Long shipmentId = someShipment.getId();
            Long auctionShipmentId = auctionShipment.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);
            Shipment shipment1 = em.find(Shipment.class, shipmentId);
            Shipment shipment2 = em.find(Shipment.class, auctionShipmentId);

            assertNull(shipment1.getAuction());
            assertEquals(shipment2.getAuction(), item);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeNonUniqueRelationship() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Item someItem = new Item("Some Item");
            em.persist(someItem);

            Shipment shipment1 = new Shipment(someItem);
            em.persist(shipment1);

            Shipment shipment2 = new Shipment(someItem);
            em.persist(shipment2);

            try {
                em.flush();
            } catch (Exception ex) {
                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
            }
        } finally {
            tm.rollback();
        }
    }
}