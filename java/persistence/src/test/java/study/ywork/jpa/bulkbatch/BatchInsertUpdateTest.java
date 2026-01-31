package study.ywork.jpa.bulkbatch;

import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.bulkbatch.Item;
import study.ywork.jpa.model.bulkbatch.User;
import study.ywork.jpa.share.util.CalendarUtil;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;

public class BatchInsertUpdateTest extends JpaManager {
    final private static Logger log = Logger.getLogger(BatchInsertUpdateTest.class.getName());

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("bulkBatch");
    }

    @Test
    public void batchInsertUpdate() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            long ONE_HUNDRED_THOUSAND = 10000;
            {
                tx.setTransactionTimeout(300);

                long startTime = new Date().getTime();
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                User tester = new User("tester");
                em.persist(tester);

                for (int i = 0; i < ONE_HUNDRED_THOUSAND; i++) {
                    Item item = new Item("Item " + i, CalendarUtil.TOMORROW.getTime(), tester);
                    em.persist(item);

                    if (i % 100 == 0) {
                        em.flush();
                        em.clear();
                    }
                }

                tx.commit();
                em.close();

                long endTime = new Date().getTime();
                log.info("### Batch insert time in seconds: " + ((endTime - startTime) / 1000));
            }
            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                assertEquals(
                        em.createQuery("select count(i) from Item i").getSingleResult(),
                        ONE_HUNDRED_THOUSAND
                );
                tx.commit();
                em.close();
            }
            {
                long startTime = new Date().getTime();
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                org.hibernate.ScrollableResults itemCursor =
                        em.unwrap(org.hibernate.Session.class)
                                .createQuery("select i from Item i")
                                .scroll(org.hibernate.ScrollMode.SCROLL_INSENSITIVE);

                int count = 0;
                while (itemCursor.next()) {
                    Item item = (Item) itemCursor.get(0);
                    modifyItem(item);

                    if (++count % 100 == 0) {
                        em.flush();
                        em.clear();
                    }
                }

                itemCursor.close();
                tx.commit();
                em.close();

                long endTime = new Date().getTime();
                log.info("### Batch update time in seconds: " + ((endTime - startTime) / 1000));
            }
            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                assertEquals(
                        em.createQuery("select count(i) from Item i where i.active = false").getSingleResult(),
                        0l
                );
                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }

    @Test
    public void batchInsertUpdateWithStatelessSession() throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            long ONE_HUNDRED_THOUSAND = 10000;
            {
                tx.begin();
                org.hibernate.SessionFactory sf =
                        jpa.getEntityManagerFactory().unwrap(org.hibernate.SessionFactory.class);
                org.hibernate.StatelessSession statelessSession = sf.openStatelessSession();

                User tester = new User("tester");
                statelessSession.insert(tester);

                for (int i = 0; i < ONE_HUNDRED_THOUSAND; i++) {
                    Item item = new Item(
                            "Item " + i, CalendarUtil.TOMORROW.getTime(), tester
                    );

                    statelessSession.insert(item);
                }

                tx.commit();
                statelessSession.close();
            }
            {
                tx.begin();
                org.hibernate.SessionFactory sf =
                        jpa.getEntityManagerFactory().unwrap(org.hibernate.SessionFactory.class);
                org.hibernate.StatelessSession statelessSession = sf.openStatelessSession();
                long count = (Long) statelessSession.createQuery("select count(i) from Item i").uniqueResult();
                assertEquals(count, ONE_HUNDRED_THOUSAND);

                tx.commit();
                statelessSession.close();
            }
            {
                long startTime = new Date().getTime();
                tx.begin();
                org.hibernate.SessionFactory sf =
                        jpa.getEntityManagerFactory().unwrap(org.hibernate.SessionFactory.class);
                org.hibernate.StatelessSession statelessSession = sf.openStatelessSession();
                org.hibernate.ScrollableResults itemCursor =
                        statelessSession
                                .createQuery("select i from Item i")
                                .scroll(org.hibernate.ScrollMode.SCROLL_INSENSITIVE);

                while (itemCursor.next()) {
                    Item item = (Item) itemCursor.get(0);
                    modifyItem(item);
                    statelessSession.update(item);
                }

                itemCursor.close();
                tx.commit();
                statelessSession.close();

                long endTime = new Date().getTime();
                log.info("### Stateless session update time in seconds: " + ((endTime - startTime) / 1000));
            }
            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                assertEquals(
                        em.createQuery("select count(i) from Item i where i.active = false").getSingleResult(),
                        0l
                );
                tx.commit();
                em.close();
            }

        } finally {
            tm.rollback();
        }
    }

    protected void modifyItem(Item item) {
        item.setActive(true);
    }
}
