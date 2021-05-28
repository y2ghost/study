package study.ywork.spring.test.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ProgramConfig.class)
@ActiveProfiles(resolver = MyProfilesResolver.class)
class MyService1Test {
    @Autowired
    private MyService myService;

    @Test
    void testDoSomething() {
        String s = myService.doSomething();
        System.out.println(s);
        assertEquals("我是MyServiceB", s);
    }
}
