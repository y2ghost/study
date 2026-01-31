package study.ywork.spring.bean.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyTwoBean implements InitializingBean, DisposableBean {
    private OtherBean otherBean;

    public MyTwoBean() {
        System.out.println("MyTwoBean constructor: " + this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet()");
    }

    @Autowired
    public void setOtherBean(OtherBean otherBean) {
        System.out.println("setOtherBean(): " + otherBean);
        this.otherBean = otherBean;
    }

    public void doSomething() {
        System.out.println("doSomething() :");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy() method");
    }
}