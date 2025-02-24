package study.ywork.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import study.ywork.users.domain.User;
import study.ywork.users.domain.UserRole;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerTests {
    @Value("${local.server.port}")
    private int port;
    private final String BASE_URL = "http://localhost:";
    private final String USERS_PATH = "/users";
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexPageShouldReturnHeaderOneContent() throws Exception {
        assertThat(this.restTemplate.getForObject(BASE_URL + port,
                String.class)).contains("Simple Users Rest Application");
    }

    @Test
    public void usersEndPointShouldReturnCollectionWithTwoUsers() throws Exception {
        Collection<User> response = this.restTemplate.
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(response.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void userEndPointPostNewUserShouldReturnUser() throws Exception {
        User user = User.builder()
                .email("dummy@email.com")
                .name("Dummy")
                .password("aw2s0meR!")
                .role(UserRole.USER)
                .active(true)
                .build();

        User response = this.restTemplate.postForObject(BASE_URL + port + USERS_PATH, user, User.class);
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        Collection<User> users = this.restTemplate.
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void userEndPointDeleteUserShouldReturnVoid() throws Exception {
        this.restTemplate.delete(BASE_URL + port + USERS_PATH + "/norma@email.com");
        Collection<User> users = this.restTemplate.
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(users).hasSizeLessThanOrEqualTo(2);
    }

    @Test
    public void userEndPointFindUserShouldReturnUser() throws Exception {
        User user = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH + "/dev@email.com", User.class);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("dev@email.com");
    }
}
