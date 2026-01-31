package study.ywork.spring.bean.scan;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MySingletonBean {

    @PostConstruct
    public void init() {
        System.out.println("单例初始化 " +
                this.getClass().getSimpleName());
    }
}