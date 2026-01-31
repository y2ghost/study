package study.ywork.mockito.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MethodProcessor1Test {
    @Test
    void processTest() {
        MethodService service = Mockito.mock(MethodService.class);
        // 调用函数则返回值10
        Mockito.when(service.doSomething()).thenReturn(10);
        MethodProcessor processor = new MethodProcessor(service);
        String returnedValue = processor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }
}
