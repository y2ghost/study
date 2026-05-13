package study.ywork.spring.example.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileResourceDemo {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    public static class Config {
        @Bean
        public ClientBean clientBean() {
            return new ClientBean();
        }

        @Bean
        public String myResourceData(@Value("classpath:yy.txt") Resource myResource) throws IOException {
            File file = myResource.getFile();
            return new String(Files.readAllBytes(file.toPath()));
        }
    }

    private static class ClientBean {
        @Value("classpath:yy.txt")
        private Resource myResource;
        @Autowired
        private String myData;

        public void doSomething() throws IOException {
            File file = myResource.getFile();
            String s = new String(Files.readAllBytes(file.toPath()));
            System.out.println(s);
            System.out.println(myData);
        }
    }
}