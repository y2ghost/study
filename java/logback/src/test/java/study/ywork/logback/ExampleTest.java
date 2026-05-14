package study.ywork.logback;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class ExampleTest {
    @Test
    public void appHasAGreeting() {
        Example classUnderTest = new Example();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
