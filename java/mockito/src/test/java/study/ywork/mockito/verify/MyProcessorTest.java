package study.ywork.mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MyProcessorTest {
    // 验证是否调用了函数
    @Test
    void processTest() {
        MyService myService = Mockito.mock(MyService.class);
        String processName = "dummy-process-name";
        Mockito.when(myService.doSomething(processName)).thenReturn(10);
        MyProcessor myProcessor = new MyProcessor(processName, myService);
        myProcessor.process();
        Mockito.verify(myService).doSomething(processName);
    }

    // 验证是否使用了参数调用了函数
    @Test
    void processTest2() {
        MyService myService = Mockito.mock(MyService.class);
        String processName = "dummy-process-name";
        Mockito.when(myService.doSomething(processName)).thenReturn(10);
        MyProcessor myProcessor = new MyProcessor(processName, myService);
        myProcessor.process();
        Mockito.verify(myService).doSomething(Mockito.anyString());
    }

    // 验证函数的调用次数、参数等
    @Test
    void processTest3() {
        MyService myService = Mockito.mock(MyService.class);
        MyProcessor myProcessor = new MyProcessor(myService);
        myProcessor.process2(0);
        myProcessor.process2(1);
        myProcessor.process2(2);
        myProcessor.process2(2);
        myProcessor.process2(2);

        Mockito.verify(myService, Mockito.atMostOnce()).doSomething2(0);
        Mockito.verify(myService, Mockito.atMostOnce()).doSomething2(1);
        Mockito.verify(myService, Mockito.atMost(3)).doSomething2(2);
        Mockito.verify(myService, Mockito.atLeastOnce()).doSomething2(1);
        Mockito.verify(myService, Mockito.atLeast(2)).doSomething2(2);
        Mockito.verify(myService, Mockito.times(3)).doSomething2(2);
        Mockito.verify(myService, Mockito.never()).doSomething2(3);
    }
}
