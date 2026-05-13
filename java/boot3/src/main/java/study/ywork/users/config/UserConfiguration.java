package study.ywork.users.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import study.ywork.users.domain.User;
import study.ywork.users.domain.UserRole;
import study.ywork.users.repository.UserCrudRepository;
import study.ywork.users.web.UsersHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class UserConfiguration {
    @Bean
    public RouterFunction<ServerResponse> userRoutes(UsersHandler usersHandler) {
        return route().nest(RequestPredicates.path("/router-users"), builder -> {
            builder.GET("", accept(APPLICATION_JSON), usersHandler::findAll);
            builder.GET("/{email}", accept(APPLICATION_JSON), usersHandler::findUserByEmail);
            builder.POST("", usersHandler::save);
            builder.DELETE("/{email}", usersHandler::deleteByEmail);
        }).build();
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> initUsers(UserCrudRepository userRepository) {
        return applicationReadyEvent -> {
            User dev = User.builder()
                    .email("dev@email.com")
                    .name("dev")
                    .password("aw2s0meR!")
                    .active(true)
                    .role(UserRole.USER)
                    .build();

            userRepository.save(dev);
            User test = User.builder()
                    .email("test@email.com")
                    .name("test")
                    .password("aw2s0meR!")
                    .active(true)
                    .role(UserRole.USER)
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(test);
        };
    }
}
