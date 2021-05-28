package study.ywork.mockito.helloworld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HelloService1Test {
    @Test
    void testDoSomething1() {
        HelloService mock = Mockito.mock(HelloService.class);
        int i = mock.doSomething();
        assertEquals(0, i);
    }
}
