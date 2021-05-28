package study.ywork.spring;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void testAppHasAGreeting() {
        App app = new App();
        assertNotNull("APP输出基本问候语", app.getGreeting());
    }
}
