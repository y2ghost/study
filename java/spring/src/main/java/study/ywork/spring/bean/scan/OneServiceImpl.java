package study.ywork.spring.bean.scan;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component("basic-service")
public class OneServiceImpl implements MyService {
    @PostConstruct
    private void init() {
        System.out.println("懒惰加载 " + this.getClass().getSimpleName());
    }

    @Override
    public String getMessage() {
        return "消息来自 " + getClass().getSimpleName();
    }

}
