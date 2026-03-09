package study.ywork.spring.provider;

public class SecondBean {
    private String arg;

    public SecondBean(String arg) {
        this.arg = arg;
    }

    public void doSomething() {
        System.out.println("在SecondBean里面，参数: " + arg);
    }
}
