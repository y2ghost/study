package study.ywork.users.event;

public class UserActivatedEvent {
    private String email;
    private boolean active;

    public UserActivatedEvent(String email, boolean active) {
        this.email = email;
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}