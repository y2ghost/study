package study.ywork.spring.example.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceLoaderDemo {
    public static void main(String[] args) throws IOException {
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
    }

    private static class ClientBean {
        @Autowired
        private ResourceLoader resourceLoader;

        public void doSomething() throws IOException {
            Resource resource = resourceLoader.getResource("classpath:yy.txt");
            File file = resource.getFile();
            String s = new String(Files.readAllBytes(file.toPath()));
            System.out.println(s);
        }
    }
}