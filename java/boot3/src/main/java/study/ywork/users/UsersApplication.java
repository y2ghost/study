package study.ywork.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UsersApplication {
    private static final Logger log = LoggerFactory.getLogger(UsersApplication.class);

    public static void main(String[] args) {
        SpringApplication app = buildApp(args);
        app.run();
    }

    // 演示构造SpringApplication
    public static SpringApplication buildApp(String[] args) {
        return new SpringApplicationBuilder().sources(UsersApplication.class)
                .logStartupInfo(true)
                .bannerMode(Banner.Mode.CONSOLE)
                .lazyInitialization(true)
                .web(WebApplicationType.SERVLET)
                .profiles("dev")
                .listeners(event -> log.info("Event: {}", event.getClass().getCanonicalName()))
                .build(args);
    }
}
