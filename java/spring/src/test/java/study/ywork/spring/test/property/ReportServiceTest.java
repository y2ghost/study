package study.ywork.spring.test.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(PropertyConfig.class)
/*
 * 可以指定内联properties属性，覆盖配置文件中的值
 * 也可以简单使用: @TestPropertySource("classpath:test.properties")
 */
@TestPropertySource(locations = "classpath:test.properties", properties = "report-subscriber=tester@example.com")
class ReportServiceTest {
    @Autowired
    private ReportService reportService;

    @Test
    void testReportSubscriber() {
        String s = reportService.getReportSubscriber();
        System.out.println(s);
        assertEquals("tester@example.com", s);
    }
}
