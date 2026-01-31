package study.ywork.mockito.helloworld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HelloProcessorTest {
    @Test
    void processTest() {
        HelloService service = Mockito.mock(HelloService.class);
        Mockito.when(service.doSomething()).thenReturn(10);
        HelloProcessor processor = new HelloProcessor(service);
        String returnedValue = processor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }
}
