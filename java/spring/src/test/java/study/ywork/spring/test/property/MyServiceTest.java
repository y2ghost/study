package study.ywork.spring.test.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(PropertyConfig.class)
class MyServiceTest {
    @Autowired
    private MyService myService;

    @Test
    @EnabledIf(expression = "#{systemProperties['os.name'].toLowerCase().contains('mac')}", reason = "Mac OS系统的测试")
    void testMyService() {
        String s = myService.getMsg();
        System.out.println(s);
        assertEquals("测试消息", s);
    }
}
