package study.ywork.spring.bean.scan;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TwoServiceImpl implements MyService {
    @PostConstruct
    private void init() {
        System.out.println("启动加载 " + this.getClass().getSimpleName());
    }

    @Override
    public String getMessage() {
        return "消息来自 " + getClass().getSimpleName();
    }
}
