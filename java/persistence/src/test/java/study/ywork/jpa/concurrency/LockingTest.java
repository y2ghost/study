package study.ywork.jpa.concurrency;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.testng.annotations.Test;
import study.ywork.jpa.env.DatabaseProduct;
import study.ywork.jpa.model.concurrency.version.Category;
import study.ywork.jpa.model.concurrency.version.Item;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockScope;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LockingTest extends VersioningTest {
    @Test
    public void pessimisticReadWrite() throws Exception {
        final ConcurrencyTestData testData = storeCategoriesAndItems();
        Long[] categories = testData.categories.identifiers;
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            BigDecimal totalPrice = new BigDecimal(0);
            for (Long categoryId : categories) {
                List<Item> items =
                        em.createQuery("select i from Item i where i.category.id = :catId")
                                .setLockMode(LockModeType.PESSIMISTIC_READ)
                                .setHint("javax.persistence.lock.timeout", 5000)
                                .setParameter("catId", categoryId)
                                .getResultList();


                for (Item item : items) {
                    totalPrice = totalPrice.add(item.getBuyNowPrice());
                }

                if (categoryId.equals(testData.categories.getFirstId())) {
                    Executors.newSingleThreadExecutor().submit(() -> {
                        UserTransaction tx1 = tm.getUserTransaction();
                        try {
                            tx1.begin();
                            EntityManager em1 = jpa.createEntityManager();
                            if (tm.databaseProduct.equals(DatabaseProduct.POSTGRESQL)) {
                                em1.unwrap(Session.class).doWork(new Work() {
                                    @Override
                                    public void execute(Connection connection) throws SQLException {
                                        connection.createStatement().execute("set statement_timeout = 5000");
                                    }
                                });
                            }

                            if (tm.databaseProduct.equals(DatabaseProduct.MARIADB)) {
                                em1.unwrap(Session.class).doWork(new Work() {
                                    @Override
                                    public void execute(Connection connection) throws SQLException {
                                        connection.createStatement().execute("set innodb_lock_wait_timeout = 5;");
                                    }
                                });
                            }

                            List<Item> items1 =
                                    em1.createQuery("select i from Item i where i.category.id = :catId")
                                            .setParameter("catId", testData.categories.getFirstId())
                                            .setLockMode(LockModeType.PESSIMISTIC_WRITE) // Prevent concurrent access
                                            .setHint("javax.persistence.lock.timeout", 5000) // Only works on Oracle...
                                            .getResultList();

                            Category lastCategory = em1.getReference(
                                    Category.class, testData.categories.getLastId()
                            );

                            items1.iterator().next().setCategory(lastCategory);

                            tx1.commit();
                            em1.close();
                        } catch (Exception ex) {
                            tm.rollback();

                            if (tm.databaseProduct.equals(DatabaseProduct.POSTGRESQL)) {
                                assertTrue(ex instanceof PersistenceException);
                            } else if (tm.databaseProduct.equals(DatabaseProduct.MARIADB)) {
                                assertTrue(ex instanceof LockTimeoutException);
                            } else {
                                assertTrue(ex instanceof LockTimeoutException);
                            }
                        }
                        return null;
                    }).get();
                }
            }

            tx.commit();
            em.close();

            assertEquals(totalPrice.compareTo(new BigDecimal("108")), 0);
        } finally {
            tm.rollback();
        }
    }

    @Test
    public void findLock() throws Exception {
        final ConcurrencyTestData testData = storeCategoriesAndItems();
        Long categoryId = testData.categories.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.lock.timeout", 5000);
            Category category =
                    em.find(
                            Category.class,
                            categoryId,
                            LockModeType.PESSIMISTIC_WRITE,
                            hints
                    );

            category.setName("New Name");
            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = {"H2", "MARIADB"})
    public void extendedLock() throws Exception {
        final ConcurrencyTestData testData = storeCategoriesAndItems();
        Long itemId = testData.items.getFirstId();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.lock.scope", PessimisticLockScope.EXTENDED);

            Item item = em.find(
                    Item.class,
                    itemId,
                    LockModeType.PESSIMISTIC_READ,
                    hints
            );

            assertEquals(item.getImages().size(), 0);
            item.setName("New Name");

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }
}
