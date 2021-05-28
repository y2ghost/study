package study.ywork.mockito.argument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class MyProcessorTest {
    @Test
    void processTest() {
        MyService myService = Mockito.mock(MyService.class);
        Mockito.when(myService.doSomething(ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection()))
            .thenReturn(10);
        MyProcessor myProcessor = new MyProcessor(myService);
        String returnedValue = myProcessor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }

    @Test
    void processTest2() {
        MyService myService = Mockito.mock(MyService.class);
        Mockito.when(myService.doSomething(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(10);
        MyProcessor myProcessor = new MyProcessor(myService);
        String returnedValue = myProcessor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }

    @Test
    void processTest3() {
        MyService myService = Mockito.mock(MyService.class);
        Mockito.when(myService.doSomething(ArgumentMatchers.anyInt())).thenReturn(5);
        MyProcessor myProcessor = new MyProcessor(myService);
        String returnedValue = myProcessor.process2();
        assertEquals("My Integer is: 5", returnedValue);
    }
}
