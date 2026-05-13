package study.ywork.spring.test.profile;

public class MyServiceA implements MyService {
    @Override
    public String doSomething() {
        return "我是" + this.getClass().getSimpleName();
    }
}
