package study.ywork.jpa.inheritance;

import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.Test;
import study.ywork.jpa.env.JpaManager;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public abstract class InheritanceCRUDTest extends JpaManager {
    public class JdbcQueryWork implements org.hibernate.jdbc.Work {

        private final boolean matchOrder;

        private final String[][] expectedRows;

        private final String sql;

        public JdbcQueryWork(String sqlResource, boolean matchOrder, String[]... expectedRows) throws IOException {
            this.sql = getTextResourceAsString(sqlResource);
            this.expectedRows = expectedRows;
            this.matchOrder = matchOrder;
        }

        private boolean assertMatch(String[] actualRow, String[] expectedRow) {
            for (int i = 0; i < expectedRow.length; i++) {
                String regex = expectedRow[i];
                String actual = actualRow[i];
                if (regex == null) {
                    if (actual == null) {
                        continue;
                    }
                    return false;
                }
                if (actual == null || !actual.matches(regex)) {
                    return false;
                }
            }
            return true;
        }

        private int assertOrderedResults(ResultSet result) throws SQLException {
            int rowIdx = 0;
            while (result.next()) {
                String[] expectedRow = this.expectedRows[rowIdx];
                for (int colNo = 0; colNo < expectedRow.length; colNo++) {
                    // JDBC的索引从1开始
                    String actual = result.getString(colNo + 1);
                    String expected = expectedRow[colNo];
                    Assert.assertTrue(actual.matches(expected));
                }
                rowIdx++;
            }
            return rowIdx;
        }

        private int assertUnorderedResults(ResultSet result) throws SQLException {
            List<String[]> expectedRowList = new LinkedList<String[]>(Arrays.asList(this.expectedRows));
            int rowCount = 0;
            while (result.next()) {
                String[] actualRow = this.getNextRow(result);
                int i = 0;
                boolean matchRow = false;
                for (String[] expectedRow : expectedRowList) {
                    if (matchRow = this.assertMatch(actualRow, expectedRow)) {
                        expectedRowList.remove(i);
                        break;
                    }
                    i++;
                }
                if (!matchRow) {
                    Assert.fail(String.format("Unexpected row: %s", Arrays.toString(actualRow)));
                }
                rowCount++;
            }
            if (!expectedRowList.isEmpty()) {
                Assert.fail("Expected rows to match: " + expectedRowList);
            }
            return rowCount;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
            PreparedStatement statement = connection.prepareStatement(this.sql);
            try {
                ResultSet result = statement.executeQuery();
                assertEquals(result.getMetaData().getColumnCount(), this.expectedRows[0].length, "Unexpected column count");
                int actualRowCount = this.matchOrder ? assertOrderedResults(result) : assertUnorderedResults(result);
                assertEquals(actualRowCount, this.expectedRows.length);
                result.close();
            } finally {
                statement.close();
            }
        }

        private String[] getNextRow(ResultSet result) throws SQLException {
            int colCount = result.getMetaData().getColumnCount();
            String[] row = new String[colCount];
            for (int colNo = 0; colNo < colCount; colNo++) {
                // JDBC index is 1-based, not 0-based.
                row[colNo] = result.getString(colNo + 1);
            }
            return row;
        }
    }

    protected abstract Object createBankAccount();

    protected abstract Object createCreditCard();

    protected void doJdbcSqlQuery(String sqlResource, boolean matchOrder, String[]... expectedRows) throws Exception {
        try (Session session = jpa.createEntityManager().unwrap(Session.class)) {
            session.doWork(new JdbcQueryWork(sqlResource, matchOrder, expectedRows));
        }
    }

    protected String getBillingDetailsQuery() {
        return "select bd from BillingDetails bd";
    }

    protected String getBankAccountsQuery() {
        return "select cc from BankAccount cc";
    }

    protected String getCreditCardsQuery() {
        return "select cc from CreditCard cc";
    }

    @Test
    public void storeBillingDetailsLoadCreditCards() throws Exception {
        storeBillingDetailsLoad(getCreditCardsQuery(), 1);
    }

    public void storeBillingDetailsLoadBankAccounts() throws Exception {
        storeBillingDetailsLoad(getBankAccountsQuery(), 1);
    }

    protected void storeBillingDetailsLoad(String sql, int expectedResultSize) throws Exception {
        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            em.persist(createCreditCard());
            em.persist(createBankAccount());

            tx.commit();
            em.close();

            tx.begin();
            em = jpa.createEntityManager();

            List billingDetails = em.createQuery(sql).getResultList();
            assertEquals(billingDetails.size(), expectedResultSize);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    public void storeLoadBillingDetails() throws Exception {
        storeBillingDetailsLoad(getBillingDetailsQuery(), 2);
    }
}
