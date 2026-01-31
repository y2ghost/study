package study.ywork.jpa.hello;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionCoordinatorBuilderImpl;
import org.hibernate.service.ServiceRegistry;
import org.testng.annotations.Test;
import study.ywork.jpa.env.TransactionManager;
import study.ywork.jpa.model.hello.Message;

import javax.transaction.UserTransaction;

import static org.testng.Assert.assertEquals;

public class HelloTest extends TransactionManager {
    protected SessionFactory createSessionFactory() {
        StandardServiceRegistryBuilder serviceRegistryBuilder =
                new StandardServiceRegistryBuilder();
        serviceRegistryBuilder
                .applySetting("hibernate.connection.datasource", "myDS")
                .applySetting("hibernate.format_sql", "true")
                .applySetting("hibernate.use_sql_comments", "true")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop");

        // 启用JTA，常见于大型项目
        serviceRegistryBuilder.applySetting(
                Environment.TRANSACTION_COORDINATOR_STRATEGY,
                JtaTransactionCoordinatorBuilderImpl.class
        );
        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Message.class);

        /**
         * <code>
         * // 添加 hbm.xml
         * metadataSources.addFile(...);
         * // 添加JAR包里面的 hbm.xml
         * metadataSources.addJar(...)
         * </code>
         */
        MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();
        Metadata metadata = metadataBuilder.build();
        assertEquals(metadata.getEntityBindings().size(), 1);
        return metadata.buildSessionFactory();
    }

    @Test
    public void storeLoadMessage() throws Exception {
        SessionFactory sessionFactory = createSessionFactory();
        try {
            {
                UserTransaction tx = tm.getUserTransaction();
                tx.begin();
                Session session = sessionFactory.getCurrentSession();
                Message message = new Message();
                message.setText("Hello World!");
                session.persist(message);
                tx.commit();
            }
        } finally {
            tm.rollback();
        }
    }
}

