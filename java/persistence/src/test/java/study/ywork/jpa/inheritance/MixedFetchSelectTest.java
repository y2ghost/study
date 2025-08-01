package study.ywork.jpa.inheritance;

import org.testng.annotations.Test;
import study.ywork.jpa.model.inheritance.mixed.BankAccount;
import study.ywork.jpa.model.inheritance.mixed.CreditCard;

public class MixedFetchSelectTest extends InheritanceCRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mixedFetchSelect", "inheritance/mixed/FetchSelect.hbm.xml");
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

    @Test
    public void jdbcBillingDetailsSqlQuery() throws Exception {
        storeLoadBillingDetails();
        doJdbcSqlQuery("inheritance/mixed/AllQuery.sql.txt", false, new String[][]{
                {"\\d*", "Jane Roe", "445566", "One Percent Bank Inc.", "999", null, null, null, "BA"},
                {"\\d*", "John Doe", null, null, null, "06", "2015", "1234123412341234", "CC"}});
    }

    @Test
    @Override
    public void storeLoadBillingDetails() throws Exception {
        super.storeLoadBillingDetails();
    }
}
