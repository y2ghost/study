package study.ywork.spring.bean.scan;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MySingletonBean {

    @PostConstruct
    public void init() {
        System.out.println("单例初始化 " +
                this.getClass().getSimpleName());
    }
}