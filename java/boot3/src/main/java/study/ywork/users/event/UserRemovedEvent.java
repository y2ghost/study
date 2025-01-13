package study.ywork.users.event;

import java.time.LocalDateTime;

public class UserRemovedEvent {
    private String email;

    private LocalDateTime removed;

    public UserRemovedEvent(String email, LocalDateTime removed) {
        this.email = email;
        this.removed = removed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRemoved() {
        return removed;
    }

    public void setRemoved(LocalDateTime removed) {
        this.removed = removed;
    }
}
