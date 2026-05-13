package study.ywork.jpa.simple;

import org.testng.annotations.Test;

public class CRUDMetadataOverrideXMLTest extends CRUDTest {
    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("simpleXMLOverride");
    }

    @Test
    @Override
    public void storeAndQueryItems() throws Exception {
        super.storeAndQueryItems();
    }

    @Test
    public void checkMetadataOverride() {
        // JPA不支持获取SQL细节，不好测试
    }
}
