package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.maps.ternary.Category;
import study.ywork.jpa.model.associations.maps.ternary.Item;
import study.ywork.jpa.model.associations.maps.ternary.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class MapsTernaryTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mapsTernary");
    }

    @Test
    public void storeLoadCategoryItems() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Category someCategory = new Category("Some Category");
            Category otherCategory = new Category("Other Category");
            em.persist(someCategory);
            em.persist(otherCategory);

            Item someItem = new Item("Some Item");
            Item otherItem = new Item("Other Item");
            em.persist(someItem);
            em.persist(otherItem);

            User someUser = new User("tester");
            em.persist(someUser);

            someCategory.getItemAddedBy().put(someItem, someUser);
            someCategory.getItemAddedBy().put(otherItem, someUser);
            otherCategory.getItemAddedBy().put(someItem, someUser);

            tx.commit();
            em.close();

            Long categoryId = someCategory.getId();
            Long otherCategoryId = otherCategory.getId();
            Long itemId = someItem.getId();
            Long userId = someUser.getId();

            tx.begin();
            em = jpa.createEntityManager();

            Category category1 = em.find(Category.class, categoryId);
            Category category2 = em.find(Category.class, otherCategoryId);

            Item item1 = em.find(Item.class, itemId);

            User user = em.find(User.class, userId);

            assertEquals(category1.getItemAddedBy().size(), 2);

            assertEquals(category2.getItemAddedBy().size(), 1);

            assertEquals(category2.getItemAddedBy().keySet().iterator().next(), item1);
            assertEquals(category2.getItemAddedBy().values().iterator().next(), user);

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}