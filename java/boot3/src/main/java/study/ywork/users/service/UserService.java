package study.ywork.users.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import study.ywork.users.domain.User;
import study.ywork.users.event.UserActivatedEvent;
import study.ywork.users.event.UserRemovedEvent;
import study.ywork.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public UserService(UserRepository userRepository, ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public Iterable<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findById(email);
    }

    public User saveUpdateUser(User user) {
        User userResult = this.userRepository.save(user);
        this.publisher.publishEvent(new UserActivatedEvent(userResult.getEmail(), userResult.isActive()));
        return userResult;
    }

    public void removeUserByEmail(String email) {
        this.userRepository.deleteById(email);
        this.publisher.publishEvent(new UserRemovedEvent(email, LocalDateTime.now()));
    }
}
