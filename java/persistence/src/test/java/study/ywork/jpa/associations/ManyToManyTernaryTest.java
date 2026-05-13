package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.manytomany.ternary.CategorizedItem;
import study.ywork.jpa.model.associations.manytomany.ternary.Category;
import study.ywork.jpa.model.associations.manytomany.ternary.Item;
import study.ywork.jpa.model.associations.manytomany.ternary.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ManyToManyTernaryTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("manyToManyTernary");
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

            CategorizedItem linkOne = new CategorizedItem(
                    someUser, someItem
            );
            someCategory.getCategorizedItems().add(linkOne);

            CategorizedItem linkTwo = new CategorizedItem(
                    someUser, otherItem
            );
            someCategory.getCategorizedItems().add(linkTwo);

            CategorizedItem linkThree = new CategorizedItem(
                    someUser, someItem
            );
            otherCategory.getCategorizedItems().add(linkThree);

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

            assertEquals(category1.getCategorizedItems().size(), 2);

            assertEquals(category2.getCategorizedItems().size(), 1);

            assertEquals(category2.getCategorizedItems().iterator().next().getItem(), item1);
            assertEquals(category2.getCategorizedItems().iterator().next().getAddedBy(), user);

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            Item item = em.find(Item.class, itemId);

            List<Category> categoriesOfItem =
                    em.createQuery(
                                    "select c from Category c " +
                                            "join c.categorizedItems ci " +
                                            "where ci.item = :itemParameter")
                            .setParameter("itemParameter", item)
                            .getResultList();

            assertEquals(categoriesOfItem.size(), 2);

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}