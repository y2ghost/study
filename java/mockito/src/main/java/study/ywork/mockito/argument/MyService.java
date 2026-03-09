package study.ywork.mockito.argument;

import java.util.Collection;

public interface MyService {
    int doSomething(String taskName, Collection<?> inputs);

    default int doSomething(int arg) {
        return arg + 10;
    }
}