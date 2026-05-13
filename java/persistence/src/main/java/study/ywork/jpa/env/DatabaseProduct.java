package study.ywork.jpa.env;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public enum DatabaseProduct {
    H2((ds, connectionURL) -> {
        ds.setClassName("org.h2.jdbcx.JdbcDataSource");
        // H2数据库连接 jdbc:h2:tcp://localhost/mem:test;USER=sa
        ds.getDriverProperties().put("URL", connectionURL != null ? connectionURL : "jdbc:h2:mem:test");
        ds.getDriverProperties().put("user", "sa");
    }, org.hibernate.dialect.H2Dialect.class.getName()),
    POSTGRESQL((ds, connectionURL) -> {
        ds.setClassName("org.postgresql.xa.PGXADataSource");
        if (connectionURL != null) {
            throw new IllegalArgumentException("doesn't support connection URLs");
        }

        ds.getDriverProperties().put("serverName", "10.0.0.2");
        ds.getDriverProperties().put("databaseName", "test");
        ds.getDriverProperties().put("user", "test");
        ds.getDriverProperties().put("password", "test");
    }, org.hibernate.dialect.PostgreSQL10Dialect.class.getName()),

    MARIADB((ds, connectionURL) -> {
        ds.setClassName("org.mariadb.jdbc.MariaDbDataSource");
        ds.getDriverProperties().put("url", connectionURL != null ? connectionURL : "jdbc:mariadb://localhost/test");
        ds.getDriverProperties().put("driverClassName", "org.mariadb.jdbc.Driver");
    }, org.hibernate.dialect.MariaDB106Dialect.class.getName());

    private final DataSourceConfiguration configuration;
    private final String hibernateDialect;

    DatabaseProduct(DataSourceConfiguration configuration,
                    String hibernateDialect) {
        this.configuration = configuration;
        this.hibernateDialect = hibernateDialect;
    }

    public DataSourceConfiguration getConfiguration() {
        return configuration;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public interface DataSourceConfiguration {
        void configure(PoolingDataSource ds, String connectionURL);
    }
}
