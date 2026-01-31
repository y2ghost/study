package study.ywork.mockito.verify;

public class MyProcessor {
    private String processName;
    private MyService myService;

    public MyProcessor(String processName, MyService myService) {
        this.processName = processName;
        this.myService = myService;
    }

    public MyProcessor(MyService myService) {
        this.processName = null;
        this.myService = myService;
    }

    public void process() {
        int returnInteger = processName != null ? myService.doSomething(processName) : -1;
        System.out.println("My Integer is: " + returnInteger);
    }

    public void process2(int n) {
        myService.doSomething2(n);
        System.out.println("Processed: " + n);
    }
}