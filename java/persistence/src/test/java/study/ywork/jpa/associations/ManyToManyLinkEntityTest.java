package study.ywork.jpa.associations;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.associations.manytomany.linkentity.CategorizedItem;
import study.ywork.jpa.model.associations.manytomany.linkentity.Category;
import study.ywork.jpa.model.associations.manytomany.linkentity.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class ManyToManyLinkEntityTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("manyToManyLinkEntity");
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

            CategorizedItem linkOne = new CategorizedItem(
                    "tester", someCategory, someItem
            );

            CategorizedItem linkTwo = new CategorizedItem(
                    "tester", someCategory, otherItem
            );

            CategorizedItem linkThree = new CategorizedItem(
                    "tester", otherCategory, someItem
            );

            em.persist(linkOne);
            em.persist(linkTwo);
            em.persist(linkThree);

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

            assertEquals(category1.getCategorizedItems().size(), 2);
            assertEquals(item1.getCategorizedItems().size(), 2);

            assertEquals(category2.getCategorizedItems().size(), 1);
            assertEquals(item2.getCategorizedItems().size(), 1);

            assertEquals(category2.getCategorizedItems().iterator().next().getItem(), item1);
            assertEquals(item2.getCategorizedItems().iterator().next().getCategory(), category1);

            tx.commit();
            em.close();

        } finally {
            tm.rollback();
        }
    }
}