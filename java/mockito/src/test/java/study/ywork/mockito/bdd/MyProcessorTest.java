package study.ywork.mockito.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

class MyProcessorTest {
    @Test
    void processTest() {
        MyService myService = Mockito.mock(MyService.class);
        BDDMockito.given(myService.doSomething()).willReturn(10);
        MyProcessor myProcessor = new MyProcessor(myService);
        String returnedValue = myProcessor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }

    public void processTest2() {
        MyService myService = Mockito.mock(MyService.class);
        BDDMockito.willReturn(10).given(myService).doSomething();
        MyProcessor myProcessor = new MyProcessor(myService);
        String returnedValue = myProcessor.process();
        assertEquals("My Integer is: 10", returnedValue);
    }

    @Test
    public void processTest3() {
        MyService myService = Mockito.mock(MyService.class);
        String processName = "dummy-process-name";
        BDDMockito.given(myService.doSomething2(processName)).willReturn(10);
        MyProcessor myProcessor = new MyProcessor(processName, myService);
        myProcessor.process2();
        BDDMockito.then(myService).should().doSomething2(processName);
    }
}
