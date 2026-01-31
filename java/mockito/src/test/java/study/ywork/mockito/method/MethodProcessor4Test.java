package study.ywork.mockito.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/*
 * 异常处理例子
 */
class MethodProcessor4Test {
    @Test
    void processTest() {
        MethodService service = Mockito.mock(MethodService.class);
        Mockito.when(service.doSomething()).thenThrow(new RuntimeException("Cannot process"));
        MethodProcessor processor = new MethodProcessor(service);

        try {
            processor.process();
            fail();
        } catch (Exception e) {
            System.out.println("-- exception thrown --");
            assertTrue(e instanceof RuntimeException);
            assertEquals(e.getMessage(), "Cannot process");
        }
    }

    @Test
    void processTest2() throws Exception {
        MethodService service = Mockito.mock(MethodService.class);
        Mockito.when(service.doSomething2()).thenThrow(new RuntimeException("Cannot process"));
        MethodProcessor processor = new MethodProcessor(service);

        try {
            processor.process2();
            fail();
        } catch (Exception e) {
            System.out.println("-- exception thrown --");
            assertTrue(e instanceof RuntimeException);
            assertEquals(e.getMessage(), "Cannot process");
        }
    }

    @Test
    void processTest3() throws Exception {
        MethodService service = Mockito.mock(MethodService.class);
        Mockito.when(service.doSomething2()).thenThrow(new RuntimeException("Cannot process"));
        MethodProcessor processor = new MethodProcessor(service);
        String returnedValue = processor.process3();
        assertEquals(returnedValue, "default-value");
    }

    @Test
    void processTest4() {
        MethodService service = Mockito.mock(MethodService.class);
        Mockito.doThrow(new RuntimeException("Cannot process")).when(service).doSomething();
        MethodProcessor processor = new MethodProcessor(service);

        try {
            processor.process();
            fail();
        } catch (Exception e) {
            System.out.println("-- exception thrown --");
            assertTrue(e instanceof RuntimeException);
            assertEquals(e.getMessage(), "Cannot process");
        }
    }
}
