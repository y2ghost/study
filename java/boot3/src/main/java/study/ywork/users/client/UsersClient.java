package study.ywork.users.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import study.ywork.users.config.MyRetroProperties;
import study.ywork.users.domain.User;

import java.text.MessageFormat;

@Component
public class UsersClient {
    private static final String USERS_URL = "/users";
    private final MyRetroProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();

    public UsersClient(MyRetroProperties properties) {
        this.properties = properties;
    }

    public User findUserByEmail(String email) {
        String uri = MessageFormat.format("http://{0}:{1}{2}/{3}",
                properties.getService().getHost(),
                String.valueOf(properties.getService().getPort()),
                USERS_URL, email);
        return restTemplate.getForObject(uri, User.class);
    }
}
