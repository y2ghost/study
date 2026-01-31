package study.ywork.jpa.inheritance;

import org.testng.annotations.Test;
import study.ywork.jpa.model.inheritance.joined.BankAccount;
import study.ywork.jpa.model.inheritance.joined.CreditCard;

public class JoinedTest extends InheritanceCRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("joined");
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
        doJdbcSqlQuery("inheritance/joined/AllQuery.sql.txt", false, new String[][]{
                {"\\d*", "Jane Roe", null, null, null, "445566", "One Percent Bank Inc.", "999", "2"},
                {"\\d*", "John Doe", "06", "2015", "1234123412341234", null, null, null, "1"}});
    }

    @Test
    public void jdbcCreditCardSqlQuery() throws Exception {
        storeLoadBillingDetails();
        doJdbcSqlQuery("inheritance/joined/CreditCard.sql.txt", false, new String[][]{
                {"\\d*", "John Doe", "06", "2015", "1234123412341234"}});
    }

    @Test
    @Override
    public void storeLoadBillingDetails() throws Exception {
        super.storeLoadBillingDetails();
    }
}
