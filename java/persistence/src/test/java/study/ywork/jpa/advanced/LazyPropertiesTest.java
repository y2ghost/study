package study.ywork.jpa.advanced;


import org.hibernate.Session;
import org.hibernate.engine.jdbc.StreamUtils;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.advanced.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Random;

import static org.testng.Assert.assertEquals;

public class LazyPropertiesTest extends JpaManager {

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("advanced");
    }

    @Test
    public void storeLoadProperties() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Item someItem = new Item();
            someItem.setName("Some item");
            someItem.setDescription("This is some description.");
            byte[] bytes = new byte[131072];
            new Random().nextBytes(bytes);
            someItem.setImage(bytes);
            em.persist(someItem);
            tx.commit();
            em.close();
            Long itemId = someItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);

            assertEquals(item.getDescription(), "This is some description.");
            assertEquals(item.getImage().length, 131072); // 128 kilobytes

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void storeLoadLocator() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        byte[] bytes = new byte[131072];

        try (InputStream imageInputStream = new ByteArrayInputStream(bytes)) {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            new Random().nextBytes(bytes);
            int byteLength = bytes.length;

            Item someItem = new Item();
            someItem.setName("Some item");
            someItem.setDescription("This is some description.");
            Session session = em.unwrap(Session.class);
            Blob blob = session.getLobHelper().createBlob(imageInputStream, byteLength);

            someItem.setImageBlob(blob);
            em.persist(someItem);
            tx.commit();
            em.close();

            Long itemId = someItem.getId();
            tx.begin();
            em = jpa.createEntityManager();
            Item item = em.find(Item.class, itemId);
            InputStream imageDataStream = item.getImageBlob().getBinaryStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            StreamUtils.copy(imageDataStream, outStream);
            byte[] imageBytes = outStream.toByteArray();
            assertEquals(imageBytes.length, 131072);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
