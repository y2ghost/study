package study.ywork.spring.example.bean;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 自定义属性值转换例子
 */
public class PropertyEditDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    @PropertySource("classpath:app.properties")
    public static class Config {
        @Bean
        public ClientBean clientBean() {
            return new ClientBean();
        }

        @Bean
        public static CustomEditorConfigurer customEditorConfigurer() {
            CustomEditorConfigurer cec = new CustomEditorConfigurer();
            cec.setPropertyEditorRegistrars(new PropertyEditorRegistrar[] { new MyCustomBeanRegistrar() });
            return cec;
        }
    }

    public static class ClientBean {
        @Value("${theTradeDate}")
        private Date tradeDate;

        public void doSomething() {
            System.out.printf("配置文件中的交易日期为 %s%n", tradeDate);
        }
    }

    public static class MyCustomBeanRegistrar implements PropertyEditorRegistrar {
        @Override
        public void registerCustomEditors(PropertyEditorRegistry registry) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setLenient(false);
            registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormatter, true));
        }
    }
}