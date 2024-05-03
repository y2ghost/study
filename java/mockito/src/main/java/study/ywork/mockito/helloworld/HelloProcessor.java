package study.ywork.mockito.helloworld;

public class HelloProcessor {
    private HelloService service;

    public HelloProcessor(HelloService service) {
        this.service = service;
    }

    public String process() {
        int returnInteger = service.doSomething();
        return String.format("My Integer is: %d", returnInteger);
    }
}