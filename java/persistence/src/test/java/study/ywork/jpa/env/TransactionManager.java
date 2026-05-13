package study.ywork.jpa.env;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Locale;

public class TransactionManager {
    public static TransactionManagerSetup tm;

    @Parameters({"database", "connectionURL"})
    @BeforeSuite()
    public void beforeSuite(@Optional String database,
                            @Optional String connectionURL) throws Exception {
        tm = new TransactionManagerSetup(
                null != database
                        ? DatabaseProduct.valueOf(database.toUpperCase(Locale.US))
                        : DatabaseProduct.H2,
                connectionURL
        );
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (tm != null) {
            tm.stop();
        }
    }
}
