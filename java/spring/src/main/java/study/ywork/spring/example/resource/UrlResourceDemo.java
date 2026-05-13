package study.ywork.spring.example.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UrlResourceDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    static class Config {
        @Bean
        public ClientBean clientBean() {
            return new ClientBean();
        }

        @Bean
        public String myResourceData(@Value("url:https://www.baidu.com/") Resource resource) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                reader.lines().forEach(stringBuilder::append);
            }

            return stringBuilder.toString();
        }
    }

    private static class ClientBean {
        @Autowired
        private String myData;

        public void doSomething() {
            System.out.println(myData);
        }
    }
}
