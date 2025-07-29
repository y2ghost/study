package study.ywork.jpa.querying.sql;

import org.hibernate.Session;
import org.testng.annotations.Test;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testng.Assert.assertEquals;

public class JDBCFallbackTest extends QueryingTest {
    public class QueryItemWork implements org.hibernate.jdbc.Work {
        final protected Long itemId;

        public QueryItemWork(Long itemId) {
            this.itemId = itemId;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                statement = connection.prepareStatement(
                        "select * from ITEM where ID = ?"
                );
                statement.setLong(1, itemId);

                result = statement.executeQuery();

                while (result.next()) {
                    String itemName = result.getString("NAME");
                    BigDecimal itemPrice = result.getBigDecimal("BUYNOWPRICE");
                    assertEquals(Long.valueOf(result.getLong("ID")), itemId);
                }
            } finally {
                if (result != null)
                    result.close();
                if (statement != null)
                    statement.close();
            }
        }
    }

    @Test
    public void queryItems() throws Exception {
        Long ITEM_ID = storeTestData().items.getFirstId();
        UserTransaction tx = tm.getUserTransaction();
        tx.begin();
        EntityManager em = jpa.createEntityManager();

        Session session = em.unwrap(Session.class);
        session.doWork(new QueryItemWork(ITEM_ID));

        tx.commit();
        em.close();
    }
}
