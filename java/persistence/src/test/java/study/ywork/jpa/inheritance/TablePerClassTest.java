package study.ywork.jpa.inheritance;

import org.testng.annotations.Test;
import study.ywork.jpa.model.inheritance.tableperclass.BankAccount;
import study.ywork.jpa.model.inheritance.tableperclass.CreditCard;

public class TablePerClassTest extends InheritanceCRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("tablePerClass");
    }

    @Override
    protected Object createBankAccount() {
        return new BankAccount(
                "Jane Roe", "445566", "One Percent Bank Inc.", "999"
        );
    }

    @Override
    protected Object createCreditCard() {
        return new CreditCard(
                "John Doe", "1234123412341234", "06", "2015"
        );
    }

    @Test(groups = {"H2", "POSTGRESQL"})
    public void jdbcBillingDetailsSqlQuery() throws Exception {
        storeLoadBillingDetails();
        doJdbcSqlQuery("inheritance/tableperclass/UnionQuery.sql.txt", false, new String[][]{
                {"\\d*", "Jane Roe", null, null, null, "445566", "One Percent Bank Inc.", "999", "2"},
                {"\\d*", "John Doe", "06", "2015", "1234123412341234", null, null, null, "1"}});
    }

    @Test
    @Override
    public void storeLoadBillingDetails() throws Exception {
        super.storeLoadBillingDetails();
    }
}
