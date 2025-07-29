package study.ywork.jpa.model.filtering.callback;

public class CurrentUser extends ThreadLocal<User> {
    public static final CurrentUser INSTANCE = new CurrentUser();

    private CurrentUser() {
    }
}
