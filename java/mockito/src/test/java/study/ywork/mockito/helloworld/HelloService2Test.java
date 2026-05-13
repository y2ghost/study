package study.ywork.mockito.helloworld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HelloService2Test {
    @Test
    void testDoSomething2() {
        HelloService mock = Mockito.mock(HelloService.class);
        Mockito.when(mock.doSomething()).thenReturn(10);
        int i = mock.doSomething();
        assertEquals(10, i);
    }
}
