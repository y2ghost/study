package study.ywork.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "User")
@Table(name = "users_user")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Name can not be empty")
    private String name;

    @Pattern(message = "Password must be at least 8 characters long and contain at least one number, one uppercase, one lowercase and one special character",
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;

    private List<UserRole> userRole;
    private boolean active;

    public User() {
        // 不做事儿
    }

    public User(UserBuilder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.name = builder.name;
        this.password = builder.password;
        this.userRole = builder.userRole;
        this.active = builder.active;
    }

    @PrePersist
    private void prePersist() {
        if (null == this.userRole) {
            this.userRole = Collections.singletonList(UserRole.USER);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<UserRole> userRole) {
        this.userRole = userRole;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String name;
        private String password;
        private List<UserRole> userRole;
        private boolean active;

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(UserRole role) {
            if (null == userRole) {
                userRole = new ArrayList<>();
            }

            userRole.add(role);
            return this;
        }

        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
