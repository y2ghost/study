package study.ywork.mockito.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MethodProcessor2Test {
    @Test
    void processTest() {
        MethodService service = Mockito.mock(MethodService.class);
        // 定义返回值为10，当service调用doSomething方法
        Mockito.doReturn(10).when(service).doSomething();
        MethodProcessor processor = new MethodProcessor(service);
        String returnedValue = processor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }
}
