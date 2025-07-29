package study.ywork.jpa.querying.sql;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.procedure.ParameterRegistration;
import org.testng.annotations.Test;
import study.ywork.jpa.model.querying.Item;
import study.ywork.jpa.querying.QueryingTest;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.UserTransaction;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test(groups = "MARIADB")
public class CallStoredProceduresTest extends QueryingTest {
    @Test(groups = "MARIADB")
    public void callReturningResultSet() throws Exception {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery(
                    "FIND_ITEMS",
                    Item.class
            );

            List<Item> result = query.getResultList();
            for (Item item : result) {
                // NOOP
            }
            assertEquals(result.size(), 3);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callReturningResultSetNative() throws Exception {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Session session = em.unwrap(Session.class);
            org.hibernate.procedure.ProcedureCall call =
                    session.createStoredProcedureCall("FIND_ITEMS", Item.class);

            org.hibernate.result.ResultSetOutput resultSetOutput =
                    (org.hibernate.result.ResultSetOutput) call.getOutputs().getCurrent();

            List<Item> result = resultSetOutput.getResultList();
            for (Item item : result) {
                // NOOP
            }
            assertEquals(result.size(), 3);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callReturningMultipleResults() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery(
                    "APPROVE_ITEMS",
                    Item.class
            );

            boolean[] expectedResults = new boolean[3];
            int i = 0;
            boolean isCurrentReturnResultSet = query.execute();

            while (true) {
                if (isCurrentReturnResultSet) {
                    List<Item> result = query.getResultList();
                    if (i == 0) {
                        assertEquals(result.size(), 1);
                    } else if (i == 1) {
                        assertEquals(result.size(), 2);
                    }
                    expectedResults[i] = true;
                } else {
                    int updateCount = query.getUpdateCount();
                    if (updateCount > -1) {
                        assertEquals(updateCount, 1);
                        expectedResults[i] = true;
                    } else {
                        break;
                    }
                }

                isCurrentReturnResultSet = query.hasMoreResults();
                i++;
            }

            for (boolean haveResult : expectedResults) {
                assertTrue(haveResult);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callReturningMultipleResultsNative() throws Exception {
        storeTestData();
        UserTransaction tx = tm.getUserTransaction();

        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Session session = em.unwrap(Session.class);
                org.hibernate.procedure.ProcedureCall call =
                        session.createStoredProcedureCall("APPROVE_ITEMS", Item.class);

                org.hibernate.procedure.ProcedureOutputs callOutputs = call.getOutputs();

                boolean[] expectedResults = new boolean[3];
                int i = 0;

                org.hibernate.result.Output output;
                while ((output = callOutputs.getCurrent()) != null) {
                    if (output.isResultSet()) {
                        List<Item> result =
                                ((org.hibernate.result.ResultSetOutput) output)
                                        .getResultList();
                        if (i == 0) {
                            assertEquals(result.size(), 1);
                        } else if (i == 1) {
                            assertEquals(result.size(), 2);
                        }
                        expectedResults[i] = true;
                    } else {
                        int updateCount =
                                ((org.hibernate.result.UpdateCountOutput) output)
                                        .getUpdateCount();
                        assertEquals(updateCount, 1);
                        expectedResults[i] = true;
                    }

                    if (!callOutputs.goToNext()) {
                        break;
                    }
                    i++;
                }

                for (boolean expectedResult : expectedResults) {
                    assertTrue(expectedResult);
                }
            }
            em.clear();

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callWithInOutParameters() throws Exception {
        TestDataCategoriesItems testDataCategoriesItems = storeTestData();
        final Long ITEM_ID = testDataCategoriesItems.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery(
                    "FIND_ITEM_TOTAL",
                    Item.class
            );

            query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);
            query.setParameter(1, ITEM_ID);

            List<Item> result = query.getResultList();
            for (Item item : result) {
                assertEquals(item.getId(), ITEM_ID);
            }

            Long totalNumberOfItems = (Long) query.getOutputParameterValue(2);

            assertEquals(result.size(), 1);
            assertEquals(totalNumberOfItems, new Long(3));

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callWithInOutParametersNative() throws Exception {
        TestDataCategoriesItems testDataCategoriesItems = storeTestData();
        final Long ITEM_ID = testDataCategoriesItems.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Session session = em.unwrap(Session.class);

            org.hibernate.procedure.ProcedureCall call =
                    session.createStoredProcedureCall("FIND_ITEM_TOTAL", Item.class);

            call.registerParameter(1, Long.class, ParameterMode.IN)
                    .bindValue(ITEM_ID);

            ParameterRegistration<Long> totalParameter =
                    call.registerParameter(2, Long.class, ParameterMode.OUT);

            org.hibernate.procedure.ProcedureOutputs callOutputs = call.getOutputs();

            boolean expectedResult = false;

            org.hibernate.result.Output output;
            while ((output = callOutputs.getCurrent()) != null) {
                if (output.isResultSet()) {
                    org.hibernate.result.ResultSetOutput resultSetOutput =
                            (org.hibernate.result.ResultSetOutput) output;
                    List<Item> result = resultSetOutput.getResultList();
                    for (Item item : result) {
                        assertEquals(item.getId(), ITEM_ID);
                    }
                    assertEquals(result.size(), 1);
                    expectedResult = true;
                }
                if (!callOutputs.goToNext())
                    break;
            }

            Long totalNumberOfItems = callOutputs.getOutputParameterValue(totalParameter);

            assertTrue(expectedResult);
            assertEquals(totalNumberOfItems, new Long(3));

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callUpdateCount() throws Exception {
        TestDataCategoriesItems testDataCategoriesItems = storeTestData();
        final Long ITEM_ID = testDataCategoriesItems.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery(
                    "UPDATE_ITEM"
            );

            query.registerStoredProcedureParameter("itemId", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
            query.setParameter("itemId", ITEM_ID);
            query.setParameter("name", "New Item Name");

            assertEquals(query.executeUpdate(), 1);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "MARIADB")
    public void callUpdateCountNative() throws Exception {
        TestDataCategoriesItems testDataCategoriesItems = storeTestData();
        final Long ITEM_ID = testDataCategoriesItems.items.getFirstId();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            Session session = em.unwrap(Session.class);
            org.hibernate.procedure.ProcedureCall call =
                    session.createStoredProcedureCall("UPDATE_ITEM");

            call.registerParameter(1, Long.class, ParameterMode.IN)
                    .bindValue(ITEM_ID);

            call.registerParameter(2, String.class, ParameterMode.IN)
                    .bindValue("New Item Name");

            org.hibernate.result.UpdateCountOutput updateCountOutput =
                    (org.hibernate.result.UpdateCountOutput) call.getOutputs().getCurrent();

            assertEquals(updateCountOutput.getUpdateCount(), 1);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "POSTGRESQL")
    public void callReturningRefCursor() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery(
                    "FIND_ITEMS",
                    Item.class
            );

            query.registerStoredProcedureParameter(
                    1,
                    void.class,
                    ParameterMode.REF_CURSOR
            );

            List<Item> result = query.getResultList();
            for (Item item : result) {
                // NOOP
            }
            assertEquals(result.size(), 3);

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    @Test(groups = "POSTGRESQL")
    public void callReturningRefCursorNative() throws Exception {
        storeTestData();

        UserTransaction tx = tm.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = jpa.createEntityManager();

            {
                Session session = em.unwrap(Session.class);

                org.hibernate.procedure.ProcedureCall call =
                        session.createStoredProcedureCall("FIND_ITEMS", Item.class);

                call.registerParameter(1, void.class, ParameterMode.REF_CURSOR);

                org.hibernate.result.ResultSetOutput resultSetOutput =
                        (org.hibernate.result.ResultSetOutput) call.getOutputs().getCurrent();

                List<Item> result = resultSetOutput.getResultList();
                for (Item item : result) {
                    // NOOP
                }
                assertEquals(result.size(), 3);
            }

            tx.commit();
            em.close();
        } finally {
            tm.rollback();
        }
    }

    public abstract class CallProcedureAndPrintResult implements Work {
        @Override
        public void execute(Connection connection) throws SQLException {
            CallableStatement statement = null;
            ResultSet result = null;
            try {
                statement = prepare(connection);
                result = execute(statement);
                if (result != null && !result.isClosed())
                    printResultSet(result);
                System.out.println("### STATEMENT UPDATE COUNT: " + statement.getUpdateCount());
            } finally {
                if (result != null)
                    result.close();
                if (statement != null)
                    statement.close();
            }

        }

        protected void printResultSet(ResultSet result) throws SQLException {
            ResultSetMetaData meta = result.getMetaData();

            System.out.println("##################################################################");
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String label = meta.getColumnLabel(i);
                System.out.println(label + " (" + meta.getColumnTypeName(i) + ")");
            }
            System.out.println("##################################################################");
            while (result.next()) {
                System.out.println("---------------------------------------------------");
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String label = meta.getColumnLabel(i);
                    System.out.print(result.getString(label) + ", ");
                }
                System.out.println();
            }
            System.out.println("##################################################################");
        }

        protected abstract CallableStatement prepare(Connection connection) throws SQLException;

        protected ResultSet execute(CallableStatement statement) throws SQLException {
            statement.execute();
            ResultSet currentResultSet = statement.getResultSet();
            printResultSet(currentResultSet);
            while (statement.getMoreResults()) {
                System.out.println("======================== NEXT RESULTSET ===========================");
                currentResultSet = statement.getResultSet();
                printResultSet(currentResultSet);
            }
            return currentResultSet;
        }
    }
}
