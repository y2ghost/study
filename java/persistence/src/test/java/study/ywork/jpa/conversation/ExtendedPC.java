package study.ywork.jpa.conversation;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.conversation.Image;
import study.ywork.jpa.model.conversation.Item;
import study.ywork.jpa.share.util.TestData;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static org.testng.Assert.assertEquals;

public class ExtendedPC extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("conversation");
    }

    @Test(groups = {"H2", "POSTGRESQL"})
    public void conversationCreateItem() throws Exception {
        Item item = new Item("Some Item");
        item.getImages().add(new Image("Foo", "foo.jpg", 800, 600));

        CreateEditItemService conversation = new CreateEditItemService();
        conversation.storeItem(item);
        conversation.commit();

        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                item = em.find(Item.class, item.getId());
                assertEquals(item.getName(), "Some Item");
                assertEquals(item.getImages().size(), 1);
            } finally {
                tm.rollback();
            }
        }
    }

    @Test(groups = {"H2", "POSTGRESQL"})
    public void conversationEditItem() throws Exception {
        final TestData testData = storeItemImagesTestData();
        Long ITEM_ID = testData.getFirstId();
        CreateEditItemService conversation = new CreateEditItemService();
        Item item = conversation.getItem(ITEM_ID);

        item.setName("New Name");
        item.getImages().add(new Image("Foo", "foo.jpg", 800, 600));

        conversation.commit();

        {
            UserTransaction tx = tm.getUserTransaction();
            try {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                item = em.find(Item.class, item.getId());
                assertEquals(item.getName(), "New Name");
                assertEquals(item.getImages().size(), 4);
            } finally {
                tm.rollback();
            }
        }
    }

    public class CreateEditItemService {
        final EntityManager em;


        public CreateEditItemService() throws Exception {
            em = jpa.createEntityManager();
        }

        protected Item getItem(Long itemId) {
            return em.find(Item.class, itemId);
        }

        protected void storeItem(Item item) {
            em.persist(item);
        }

        protected void commit() throws Exception {
            try {
                UserTransaction tx = tm.getUserTransaction();
                tx.begin();
                em.joinTransaction();
                tx.commit();
            } finally {
                em.close();
            }
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

    protected byte[] serialize(Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            return bos.toByteArray();
        } finally {
            if (out != null)
                out.close();
            bos.close();
        }
    }

    protected Object deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } finally {
            bis.close();
            if (in != null)
                in.close();
        }
    }
}
