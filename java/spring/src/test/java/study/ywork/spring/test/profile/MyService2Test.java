package study.ywork.spring.test.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = ProgramConfig.class, initializers = MyContextInitializer.class)
@ActiveProfiles(resolver = MyProfilesResolver.class)
class MyService2Test {
    @Autowired
    private MyService myService;

    @Test
    void testDoSomething() {
        String s = myService.doSomething();
        System.out.println(s);
        assertEquals("我是MyServiceA", s);
    }
}
