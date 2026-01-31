package study.ywork.jpa.model.filtering.callback;

import java.util.ArrayList;

public class Mail extends ArrayList<String> {
    public static final Mail INSTANCE = new Mail();

    private Mail() {
    }

    public void send(String message) {
        add(message);
    }
}
