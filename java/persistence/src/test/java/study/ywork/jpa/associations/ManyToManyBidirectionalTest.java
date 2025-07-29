package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.manytomany.bidirectional.Category;
import study.ywork.jpa.model.associations.manytomany.bidirectional.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class ManyToManyBidirectionalTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("manyToManyBidirectional");
    }

    @Test
    public void storeLoadCategoryItems() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Category someCategory = new Category("Some Category");
            Category otherCategory = new Category("Other Category");

            Item someItem = new Item("Some Item");
            Item otherItem = new Item("Other Item");

            someCategory.getItems().add(someItem);
            someItem.getCategories().add(someCategory);

            someCategory.getItems().add(otherItem);
            otherItem.getCategories().add(someCategory);

            otherCategory.getItems().add(someItem);
            someItem.getCategories().add(otherCategory);

            em.persist(someCategory);
            em.persist(otherCategory);

            tx.commit();
            em.close();

            Long categoryId = someCategory.getId();
            Long otherCategoryId = otherCategory.getId();
            Long itemId = someItem.getId();
            Long otherItemId = otherItem.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Category category1 = em.find(Category.class, categoryId);
            Category category2 = em.find(Category.class, otherCategoryId);

            Item item1 = em.find(Item.class, itemId);
            Item item2 = em.find(Item.class, otherItemId);

            assertEquals(category1.getItems().size(), 2);
            assertEquals(item1.getCategories().size(), 2);

            assertEquals(category2.getItems().size(), 1);
            assertEquals(item2.getCategories().size(), 1);

            assertEquals(category2.getItems().iterator().next(), item1);
            assertEquals(item2.getCategories().iterator().next(), category1);

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}