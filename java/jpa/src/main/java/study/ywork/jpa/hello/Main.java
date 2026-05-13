package study.ywork.jpa.hello;

import static java.lang.System.out;
import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_PASSWORD;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_URL;
import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_USER;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;

import org.hibernate.cfg.Configuration;

public class Main {
	public static void main(String[] args) {
		var sessionFactory = new Configuration().addAnnotatedClass(Book.class)
				.setProperty(JAKARTA_JDBC_URL, "jdbc:h2:mem:db1").setProperty(JAKARTA_JDBC_USER, "sa")
				.setProperty(JAKARTA_JDBC_PASSWORD, "").setProperty(SHOW_SQL, true).setProperty(FORMAT_SQL, true)
				.setProperty(HIGHLIGHT_SQL, true).buildSessionFactory();

		sessionFactory.getSchemaManager().exportMappedObjects(true);
		sessionFactory.inTransaction(session -> session.persist(new Book("9781932394153", "Hibernate in Action")));
		sessionFactory.inSession(session -> out.println(
				session.createSelectionQuery("select isbn||': '||title from Book", String.class).getSingleResult()));
		sessionFactory.inSession(session -> {
			var builder = sessionFactory.getCriteriaBuilder();
			var query = builder.createQuery(String.class);
			var book = query.from(Book.class);
			query.select(
					builder.concat(builder.concat(book.get(Book_.isbn), builder.literal(": ")), book.get(Book_.title)));
			out.println(session.createSelectionQuery(query).getSingleResult());
		});
	}
}
