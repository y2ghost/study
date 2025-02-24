package study.ywork.users.repository;

import org.springframework.stereotype.Component;
import study.ywork.users.domain.User;
import study.ywork.users.domain.UserRole;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepository implements SimpleRepository<User, String> {
    private final Map<String, User> users;

    public UserRepository() {
        users = new HashMap<>();
        users.put("dev@email.com", User.builder()
                .email("dev@email.com")
                .name("dev")
                .password("aw2s0meR!")
                .role(UserRole.USER)
                .active(true)
                .build());
        users.put("test@email.com", User.builder()
                .name("test")
                .email("test@email.com")
                .password("aw2s0meR!")
                .role(UserRole.USER)
                .role(UserRole.ADMIN)
                .active(true)
                .build());
    }

    @Override
    public User save(User user) {
        if (user.getUserRole() == null) {
            user.setUserRole(Collections.emptyList());
        }

        return this.users.put(user.getEmail(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.of(this.users.get(id));
    }

    @Override
    public Iterable<User> findAll() {
        return this.users.values();
    }

    @Override
    public void deleteById(String id) {
        this.users.remove(id);
    }
}
