package study.ywork.spring.hello.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHi(String name) {
        System.out.println("hello " + name);
    }
}
