package study.ywork.users.exception;

public class RetroBoardNotFoundException extends RuntimeException {
    public RetroBoardNotFoundException() {
        super("RetroBoard Not Found");
    }

    public RetroBoardNotFoundException(String message) {
        super(String.format("RetroBoard Not Found: %s", message));
    }

    public RetroBoardNotFoundException(String message, Throwable cause) {
        super(String.format("RetroBoard Not Found: %s", message), cause);
    }
}
