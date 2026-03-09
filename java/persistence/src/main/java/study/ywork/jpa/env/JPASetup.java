package study.ywork.jpa.env;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPASetup {
    protected final String persistenceUnitName;
    protected final Map<String, String> properties = new HashMap<>();
    protected final EntityManagerFactory entityManagerFactory;

    public JPASetup(DatabaseProduct databaseProduct,
                    String persistenceUnitName,
                    String... hbmResources) {

        this.persistenceUnitName = persistenceUnitName;
        String hbmFiles = String.join(",", hbmResources != null ? hbmResources : new String[0]);
        properties.put("hibernate.archive.autodetection", "none");
        properties.put("hibernate.hbm_xml_files", hbmFiles);
        // 通用配置
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.dialect", databaseProduct.getHibernateDialect());
        entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public void createSchema() {
        generateSchema("create");
    }

    public void dropSchema() {
        generateSchema("drop");
    }

    public void generateSchema(String action) {
        Map<String, String> createSchemaProperties = new HashMap<>(properties);
        createSchemaProperties.put("javax.persistence.schema-generation.database.action", action);
        Persistence.generateSchema(getPersistenceUnitName(), createSchemaProperties);
    }
}
