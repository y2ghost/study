package study.ywork.jpa.simple;

import org.testng.annotations.Test;

public class CRUDMetadataHBMXMLTest extends CRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simpleXMLHibernate", "simple/Native.hbm.xml");
    }

    @Test
    @Override
    public void storeAndQueryItems() throws Exception {
        super.storeAndQueryItems("findItemsHibernate");
    }
}
