package study.ywork.mockito.spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MyCalcTest {
    @Test
    void multiplyTest() {
        MyCalc myCalcSpy = Mockito.spy(MyCalc.class);
        int result = myCalcSpy.multiple(3, 2);
        assertEquals(6, result);

        MyCalc myCalcMock = Mockito.mock(MyCalc.class);
        int result2 = myCalcMock.multiple(3, 2);
        assertEquals(0, result2);
    }

    @Test
    public void multiplyTest2() {
        MyCalc myCalcMock = Mockito.mock(MyCalc.class);
        Mockito.when(myCalcMock.multiple(3, 2)).thenCallRealMethod();
        int result2 = myCalcMock.multiple(3, 2);
        assertEquals(6, result2);
    }

    @Test
    public void multiplyTest3() {
        MyCalc myCalcSpy = Mockito.spy(MyCalc.class);
        Mockito.when(myCalcSpy.add(3, 2)).thenReturn(100);

        int result = myCalcSpy.multiple(3, 2);
        assertEquals(6, result);

        int result2 = myCalcSpy.add(3, 2);
        assertEquals(100, result2);
    }
}
