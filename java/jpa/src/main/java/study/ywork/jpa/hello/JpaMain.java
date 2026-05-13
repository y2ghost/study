package study.ywork.jpa.hello;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static java.lang.System.out;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;
import static org.hibernate.tool.schema.Action.CREATE;

import java.util.Map;
import java.util.function.Consumer;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.hibernate.tool.schema.Action;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class JpaMain {
	public static void main(String[] args) {
		var factory = createEntityManagerFactory("example", Map.of(JAKARTA_HBM2DDL_DATABASE_ACTION, CREATE));

		inSession(factory, entityManager -> entityManager.persist(new Book("9781932394153", "Hibernate in Action")));
		inSession(factory, entityManager -> out.println(
				entityManager.createQuery("select isbn||': '||title from Book", String.class).getSingleResult()));
		inSession(factory, entityManager -> {
			var builder = factory.getCriteriaBuilder();
			var query = builder.createQuery(String.class);
			var book = query.from(Book.class);
			query.select(
					builder.concat(builder.concat(book.get(Book_.isbn), builder.literal(": ")), book.get(Book_.title)));
			out.println(entityManager.createQuery(query).getSingleResult());
		});
	}

	static void inSession(EntityManagerFactory factory, Consumer<EntityManager> work) {
		var entityManager = factory.createEntityManager();
		var transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			work.accept(entityManager);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive())
				transaction.rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

	public SessionFactory configByCode() {
		// 配置等同persistence-postgresql.xml
		return new Configuration().addAnnotatedClass(Book.class)
				.setProperty(JdbcSettings.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost/example")
				.setProperty(JdbcSettings.JAKARTA_JDBC_USER, "test")
				.setProperty(JdbcSettings.JAKARTA_JDBC_PASSWORD, "123456")
				.setProperty(SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.SPEC_ACTION_DROP_AND_CREATE)
				.setProperty(JdbcSettings.SHOW_SQL, true).setProperty(JdbcSettings.FORMAT_SQL, true)
				.setProperty(JdbcSettings.HIGHLIGHT_SQL, true).buildSessionFactory();
	}
}