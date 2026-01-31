package study.ywork.mockito.argument;

import java.util.Arrays;

public class MyProcessor {
    private MyService myService;

    public MyProcessor(MyService myService) {
        this.myService = myService;
    }

    public String process() {
        int returnInteger = myService.doSomething("my-process-task", Arrays.asList(1, 3, 5));
        return formatInteger(returnInteger);
    }

    public String process2() {
        int returnInteger = myService.doSomething(10);
        return formatInteger(returnInteger);
    }

    private String formatInteger(int returnInteger) {
        return String.format("My Integer is: %d", returnInteger);
    }
}