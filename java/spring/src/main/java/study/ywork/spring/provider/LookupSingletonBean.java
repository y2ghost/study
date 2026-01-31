package study.ywork.spring.provider;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class LookupSingletonBean {
    public void showMessage() {
        LookupPrototypeBean bean = getPrototypeBean();
        System.out.println("你好，为您报时 " + bean.getDateTime());
    }

    @Lookup
    public LookupPrototypeBean getPrototypeBean() {
        // Spring代理后将会重写该方法
        return null;
    }
}
