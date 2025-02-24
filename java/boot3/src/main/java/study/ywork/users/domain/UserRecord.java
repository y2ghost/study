package study.ywork.users.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record UserRecord(Long id,
                         String email,
                         String name,
                         String password,
                         boolean active,
                         List<UserRole> userRole) {
    public UserRecord {
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);
        Objects.requireNonNull(password);
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one number, one uppercase, one lowercase and one special character");
        }

        pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }

        if (null == userRole) {
            userRole = new ArrayList<>();
            userRole.add(UserRole.INFO);
        }
    }

    public UserRecord withId(Long id) {
        return new UserRecord(id, this.email(), this.name(), this.password(), this.active(), this.userRole());
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

        public UserBuilder userRole(List<UserRole> roles) {
            this.userRole = roles;
            return this;
        }

        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public UserRecord build() {
            return new UserRecord(this.id, this.email, this.name, this.password, this.active, this.userRole);
        }
    }
}
