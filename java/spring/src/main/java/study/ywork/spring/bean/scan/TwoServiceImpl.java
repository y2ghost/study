package study.ywork.spring.bean.scan;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

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
