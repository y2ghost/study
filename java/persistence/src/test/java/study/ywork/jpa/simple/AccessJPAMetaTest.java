package study.ywork.jpa.simple;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.simple.Item;
import study.ywork.jpa.model.simple.Item_;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class AccessJPAMetaTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simpleXMLComplete");
    }

    @Test
    public void accessDynamicMetamodel() {
        EntityManagerFactory entityManagerFactory = jpa.getEntityManagerFactory();
        Metamodel mm = entityManagerFactory.getMetamodel();
        Set<ManagedType<?>> managedTypes = mm.getManagedTypes();
        assertEquals(managedTypes.size(), 1);

        ManagedType itemType = managedTypes.iterator().next();
        assertEquals(itemType.getPersistenceType(), Type.PersistenceType.ENTITY);

        SingularAttribute nameAttribute = itemType.getSingularAttribute("name");
        assertEquals(nameAttribute.getJavaType(), String.class);
        assertEquals(nameAttribute.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC);
        assertFalse(nameAttribute.isOptional());

        SingularAttribute auctionEndAttribute = itemType.getSingularAttribute("auctionEnd");
        assertEquals(auctionEndAttribute.getJavaType(), Date.class);
        assertFalse(auctionEndAttribute.isCollection());
        assertFalse(auctionEndAttribute.isAssociation());
    }

    @Test
    public void accessStaticMetamodel() {
        SingularAttribute nameAttribute = Item_.name;
        assertEquals(nameAttribute.getJavaType(), String.class);
    }

    @Test
    public void queryStaticMetamodel() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager entityManager = jpa.createEntityManager();

            Item itemOne = new Item();
            itemOne.setName("This is some item");
            itemOne.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));
            entityManager.persist(itemOne);

            Item itemTwo = new Item();
            itemTwo.setName("Another item");
            itemTwo.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));

            entityManager.persist(itemTwo);
            tx.commit();
            entityManager.close();

            entityManager = jpa.createEntityManager();
            tx.begin();

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Item> query = cb.createQuery(Item.class);
            Root<Item> fromItem = query.from(Item.class);
            query.select(fromItem);

            List<Item> items = entityManager.createQuery(query).getResultList();
            assertEquals(items.size(), 2);

            Path<String> namePath = fromItem.get("name");
            query.where(
                    cb.like(namePath, cb.parameter(String.class, "pattern"))
            );

            items = entityManager.createQuery(query)
                    .setParameter("pattern", "%some item%")
                    .getResultList();

            assertEquals(items.size(), 1);
            assertEquals(items.iterator().next().getName(), "This is some item");

            query.where(
                    cb.like(fromItem.get(Item_.name), cb.parameter(String.class, "pattern"))
            );

            items = entityManager.createQuery(query)
                    .setParameter("pattern", "%some item%")
                    .getResultList();

            assertEquals(items.size(), 1);
            assertEquals(items.iterator().next().getName(), "This is some item");

            tx.commit();
            entityManager.close();
        } finally {
            tm.rollback();
        }
    }
}
