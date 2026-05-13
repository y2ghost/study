package study.ywork.jpa.env;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class JpaManager extends TransactionManager {
    public String persistenceUnitName;
    public String[] hbmResources;
    public JPASetup jpa;

    @BeforeClass
    public void beforeClass() {
        configurePersistenceUnit();
    }

    public void configurePersistenceUnit() {
        configurePersistenceUnit(null);
    }

    public void configurePersistenceUnit(String persistenceUnitName,
                                         String... hbmResources) {
        this.persistenceUnitName = persistenceUnitName;
        this.hbmResources = hbmResources;
    }

    @BeforeMethod
    public void beforeMethod() {
        jpa = new JPASetup(tm.databaseProduct, persistenceUnitName, hbmResources);
        jpa.dropSchema();
        jpa.createSchema();
        afterJPABootstrap();
    }

    public void afterJPABootstrap() {
        // NOOP
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        if (null != jpa) {
            beforeJPAClose();
            if (!"true".equals(System.getProperty("keepSchema"))) {
                jpa.dropSchema();
            }

            jpa.getEntityManagerFactory().close();
        }
    }

    public void beforeJPAClose() {
        // NOOP
    }

    protected long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];
        long count = 0;
        int n;

        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    protected String getTextResourceAsString(String resource) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
        if (is == null) {
            throw new IllegalArgumentException("Resource not found: " + resource);
        }

        StringWriter sw = new StringWriter();
        copy(new InputStreamReader(is), sw);
        return sw.toString();
    }

    protected Throwable unwrapRootCause(Throwable throwable) {
        return unwrapCauseOfType(throwable, null);
    }

    protected Throwable unwrapCauseOfType(Throwable throwable, Class<? extends Throwable> type) {
        for (Throwable current = throwable; current != null; current = current.getCause()) {
            if (type != null && type.isAssignableFrom(current.getClass())) {
                return current;
            }

            throwable = current;
        }

        return throwable;
    }
}
