package study.ywork.mockito.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/*
 * 连续调用返回值例子
 */
class MethodProcessor3Test {
    @Test
    void processTest() {
        MethodService service = Mockito.mock(MethodService.class);
        Mockito.when(service.doSomething()).thenReturn(10, 20);
        MethodProcessor processor = new MethodProcessor(service);
        String returnedValue = processor.process();
        assertEquals("My Integer is: 10", returnedValue);
        returnedValue = processor.process();
        assertEquals("My Integer is: 20", returnedValue);
    }
    
    @Test
    void processTest2() {
        MethodService service = Mockito.mock(MethodService.class);
        Mockito.when(service.doSomething()).thenReturn(10).thenReturn(20);
        MethodProcessor processor = new MethodProcessor(service);
        String returnedValue = processor.process();
        assertEquals("My Integer is: 10", returnedValue);
        returnedValue = processor.process();
        assertEquals("My Integer is: 20", returnedValue);
    }
}
