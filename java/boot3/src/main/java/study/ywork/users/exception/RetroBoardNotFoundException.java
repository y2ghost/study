package study.ywork.users.exception;

public class RetroBoardNotFoundException extends RuntimeException {
    public RetroBoardNotFoundException() {
        super("RetroBoard Not Found");
    }
}
