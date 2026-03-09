package study.ywork.users.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.users.domain.Card;
import study.ywork.users.domain.CardType;
import study.ywork.users.domain.RetroBoard;
import study.ywork.users.service.RetroBoardService;

import java.util.Arrays;

@EnableConfigurationProperties({MyRetroProperties.class})
@Configuration
public class MyRetroConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MyRetroConfiguration.class);
    private final MyRetroProperties properties;

    public MyRetroConfiguration(MyRetroProperties properties) {
        this.properties = properties;
    }

    /**
     * 适合springboot准备好了Bean对象的加载和连接后执行
     */
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            String info = Arrays.toString(args);
            log.info("[CLR] Args: {}", info);
        };
    }

    /**
     * 适合springboot准备好了Bean对象的加载和连接后执行，但在CommandLineRunner之前
     */
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            log.info("[AR] Option Args: {}", args.getOptionNames());
            log.info("[AR] Option Arg Values: {}", args.getOptionValues("option"));
            log.info("[AR] Non Option: {}", args.getNonOptionArgs());
        };
    }

    /**
     * 适合springboot可以准备服务后执行
     */
    @Bean
    ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener() {
        return event -> {
            log.info("[AL] Im ready to interact...");
            log.info("[AL] properties {} ", properties);
        };
    }


    @Bean
    ApplicationListener<ApplicationReadyEvent> initRetroBoards(RetroBoardService retroBoardService) {
        return applicationReadyEvent -> {
            RetroBoard retroBoard = retroBoardService.save(new RetroBoard("Spring Boot Conference"));
            retroBoardService.addCardToRetroBoard(retroBoard.getId(), Card.builder().comment("Spring Boot Rocks!").cardType(CardType.HAPPY).retroBoard(retroBoard).build());
            retroBoardService.addCardToRetroBoard(retroBoard.getId(), Card.builder().comment("Meet everyone in person").cardType(CardType.HAPPY).retroBoard(retroBoard).build());
            retroBoardService.addCardToRetroBoard(retroBoard.getId(), Card.builder().comment("When is the next one?").cardType(CardType.MEH).retroBoard(retroBoard).build());
            retroBoardService.addCardToRetroBoard(retroBoard.getId(), Card.builder().comment("Not enough time to talk to everyone").cardType(CardType.SAD).retroBoard(retroBoard).build());
        };
    }
}
