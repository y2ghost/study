package study.ywork.jpa.inheritance;

import org.testng.annotations.Test;
import study.ywork.jpa.model.inheritance.mappedsuperclass.BankAccount;
import study.ywork.jpa.model.inheritance.mappedsuperclass.BillingDetails;
import study.ywork.jpa.model.inheritance.mappedsuperclass.CreditCard;

public class MappedSuperclassTest extends InheritanceCRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("mappedSuperclass");
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

    @Override
    protected String getBillingDetailsQuery() {
        return "select bd from " + BillingDetails.class.getName() + " bd";
    }

    @Test
    public void jdbcSqlQueryBankAccount() throws Exception {
        storeLoadBillingDetails();
        doJdbcSqlQuery("inheritance/mappedsuperclass/BankAccount.sql.txt",
                true, new String[]{"\\d*", "Jane Roe", "445566", "One Percent Bank Inc.", "999"});
    }

    @Test
    public void jdbcSqlQueryCreditCard() throws Exception {
        storeLoadBillingDetails();
        doJdbcSqlQuery("inheritance/mappedsuperclass/CreditCard.sql.txt",
                true, new String[]{"\\d*", "John Doe", "1234123412341234", "06", "2015"});
    }

    @Test
    @Override
    public void storeLoadBillingDetails() throws Exception {
        super.storeLoadBillingDetails();
    }
}
