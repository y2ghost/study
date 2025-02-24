package study.ywork.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ywork.users.web.UsersHandler;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UsersSmokeTests {

    @Autowired
    private UsersHandler controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
