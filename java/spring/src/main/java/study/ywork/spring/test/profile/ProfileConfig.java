package study.ywork.spring.test.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {
    @Bean
    @Profile(PROFILE_LOCAL)
    public DataService dataServiceLocal() {
        return DataServiceLocal.INSTANCE;
    }

    @Bean
    @Profile(PROFILE_REMOTE)
    public DataService dataServiceRemote() {
        return DataServiceRemote.INSTANCE;
    }

    public static final String PROFILE_LOCAL = "local";
    public static final String PROFILE_REMOTE = "remote";
}
