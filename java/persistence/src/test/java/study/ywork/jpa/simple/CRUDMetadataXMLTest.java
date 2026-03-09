package study.ywork.jpa.simple;

import org.testng.annotations.Test;

public class CRUDMetadataXMLTest extends CRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simpleXMLComplete");
    }

    @Test
    @Override
    public void storeAndQueryItems() throws Exception {
        super.storeAndQueryItems();
    }
}
