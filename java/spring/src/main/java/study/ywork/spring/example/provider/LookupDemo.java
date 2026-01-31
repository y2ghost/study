package study.ywork.spring.example.provider;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import study.ywork.spring.provider.LookupPrototypeBean;
import study.ywork.spring.provider.LookupSingletonBean;

@Configuration
@ComponentScan(basePackageClasses = LookupSingletonBean.class)
public class LookupDemo {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LookupPrototypeBean prototypeBean() {
        return new LookupPrototypeBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LookupDemo.class);
        LookupSingletonBean bean = context.getBean(LookupSingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(LookupSingletonBean.class);
        bean.showMessage();
        context.close();
    }
}
