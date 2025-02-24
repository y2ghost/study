package study.ywork.spring.example.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import study.ywork.spring.config.MultiClient;
import study.ywork.spring.config.MyDataConfig;
import study.ywork.spring.config.MyDataSource;

/*
 * 导入配置类例子
 */
@Configuration
@Import(MyDataConfig.class)
public class ImportConfigDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportConfigDemo.class,
            MyDataConfig.class);
        MultiClient client = context.getBean(MultiClient.class);
        client.showData();
        context.close();
    }

    @Bean
    MultiClient getMultiClient(MyDataSource myDataSource) {
        return new MultiClient(myDataSource);
    }
}
