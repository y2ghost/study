package study.ywork.spring.bean.lifecycle;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class MyOneBean {
    private OtherBean otherBean;

    public MyOneBean() {
        System.out.println("MyOneBean constructor: " + this);
    }

    // BEAN生成后的初始化函数
    @PostConstruct
    public void myPostConstruct() {
        System.out.println("myPostConstruct()");
    }

    @Autowired
    public void setOtherBean(OtherBean otherBean) {
        System.out.println("setOtherBean(): " + otherBean);
        this.otherBean = otherBean;
    }

    public void doSomething() {
        System.out.println("doSomething()");
    }

    // BEAN销毁前的清理函数
    @PreDestroy
    public void cleanUp() {
        System.out.println("cleanUp method");
    }
}