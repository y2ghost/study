package study.ywork.test.assertions;

public class NoJobException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    NoJobException(String message) {
        super(message);
    }
}
