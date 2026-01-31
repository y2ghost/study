package study.ywork.jpa.filtering;

import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;
import study.ywork.jpa.model.filtering.envers.Item;
import study.ywork.jpa.model.filtering.envers.User;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class EnversTest extends JpaManager {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("filteringEnvers");
    }

    @Test
    public void auditLogging() throws Throwable {
        UserTransaction tx = tm.getUserTransaction();
        try {
            Long itemId;
            Long userId;
            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                User user = new User("tester");
                em.persist(user);

                Item item = new Item("Foo", user);
                em.persist(item);

                tx.commit();
                em.close();

                itemId = item.getId();
                userId = user.getId();
            }

            Date timestampCreate = new Date();

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();

                Item item = em.find(Item.class, itemId);
                item.setName("Bar");
                item.getSeller().setUsername("boy");

                tx.commit();
                em.close();
            }

            Date timestampUpdate = new Date();

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                Item item = em.find(Item.class, itemId);
                em.remove(item);

                tx.commit();
                em.close();
            }

            Date timestampDelete = new Date();

            {
                tx.begin();
                EntityManager em = jpa.createEntityManager();
                AuditReader auditReader = AuditReaderFactory.get(em);

                Number revisionCreate = auditReader.getRevisionNumberForDate(timestampCreate);
                Number revisionUpdate = auditReader.getRevisionNumberForDate(timestampUpdate);
                Number revisionDelete = auditReader.getRevisionNumberForDate(timestampDelete);

                List<Number> itemRevisions = auditReader.getRevisions(Item.class, itemId);
                assertEquals(itemRevisions.size(), 3);
                for (Number itemRevision : itemRevisions) {
                    Date itemRevisionTimestamp = auditReader.getRevisionDate(itemRevision);
                    System.out.println(itemRevisionTimestamp.toString());
                }

                List<Number> userRevisions = auditReader.getRevisions(User.class, userId);
                assertEquals(userRevisions.size(), 2);

                em.clear();
                {
                    AuditQuery query = auditReader.createQuery()
                            .forRevisionsOfEntity(Item.class, false, false);
                    List<Object[]> result = query.getResultList();

                    for (Object[] tuple : result) {
                        Item item = (Item) tuple[0];
                        DefaultRevisionEntity revision = (DefaultRevisionEntity) tuple[1];
                        RevisionType revisionType = (RevisionType) tuple[2];

                        if (revision.getId() == 1) {
                            assertEquals(revisionType, RevisionType.ADD);
                            assertEquals(item.getName(), "Foo");
                        } else if (revision.getId() == 2) {
                            assertEquals(revisionType, RevisionType.MOD);
                            assertEquals(item.getName(), "Bar");
                        } else if (revision.getId() == 3) {
                            assertEquals(revisionType, RevisionType.DEL);
                            assertNull(item);
                        }
                    }
                }
                em.clear();
                {
                    Item item = auditReader.find(Item.class, itemId, revisionCreate);
                    assertEquals(item.getName(), "Foo");
                    assertEquals(item.getSeller().getUsername(), "tester");

                    Item modifiedItem = auditReader.find(Item.class, itemId, revisionUpdate);
                    assertEquals(modifiedItem.getName(), "Bar");
                    assertEquals(modifiedItem.getSeller().getUsername(), "boy");

                    Item deletedItem = auditReader.find(Item.class, itemId, revisionDelete);
                    assertNull(deletedItem);

                    User user = auditReader.find(User.class, userId, revisionDelete);
                    assertEquals(user.getUsername(), "boy");
                }
                em.clear();
                {
                    AuditQuery query = auditReader.createQuery()
                            .forEntitiesAtRevision(Item.class, revisionUpdate);
                    query.add(
                            AuditEntity.property("name").like("Ba", MatchMode.START)
                    );

                    query.add(
                            AuditEntity.relatedId("seller").eq(userId)
                    );

                    query.addOrder(
                            AuditEntity.property("name").desc()
                    );

                    query.setFirstResult(0);
                    query.setMaxResults(10);

                    assertEquals(query.getResultList().size(), 1);
                    Item result = (Item) query.getResultList().get(0);
                    assertEquals(result.getSeller().getUsername(), "boy");
                }
                em.clear();
                {
                    AuditQuery query = auditReader.createQuery()
                            .forEntitiesAtRevision(Item.class, revisionUpdate);

                    query.addProjection(
                            AuditEntity.property("name")
                    );

                    assertEquals(query.getResultList().size(), 1);
                    String result = (String) query.getSingleResult();
                    assertEquals(result, "Bar");
                }
                em.clear();

                tx.commit();
                em.close();
            }
        } finally {
            tm.rollback();
        }
    }
}
