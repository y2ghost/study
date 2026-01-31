package study.ywork.spring.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import study.ywork.spring.config.MultiClient;
import study.ywork.spring.config.MyDataConfig;

/*
 * 加载多个配置类例子
 */
@Configuration
public class MultiConfigDemo {
    // 配置类也支持自动注入，此处代码用于演示
    @Autowired
    private MyDataConfig myDataConfig;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MultiConfigDemo.class,
            MyDataConfig.class);
        MultiClient client = context.getBean(MultiClient.class);
        client.showData();
        context.close();
    }

    @Bean
    MultiClient getMultiClient() {
        return new MultiClient(myDataConfig.getDataSource());
    }
}
