package study.ywork.mockito.bdd;

public class MyProcessor {
    private String processName;
    private MyService myService;

    public MyProcessor(MyService myService) {
        this(null, myService);
    }

    public MyProcessor(String processName, MyService myService) {
        this.processName = processName;
        this.myService = myService;
    }

    public String process() {
        int returnInteger = myService.doSomething();
        return String.format("My Integer is: %d", returnInteger);
    }

    public void process2() {
        int returnInteger = processName != null ? myService.doSomething2(processName) : -1;
        System.out.println("My Integer is: " + returnInteger);
    }
}
