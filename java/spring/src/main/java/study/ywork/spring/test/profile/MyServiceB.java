package study.ywork.spring.test.profile;

public class MyServiceB implements MyService {
    @Override
    public String doSomething() {
        return "我是" + this.getClass().getSimpleName();
    }
}
