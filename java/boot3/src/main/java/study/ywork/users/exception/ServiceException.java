package study.ywork.users.exception;

public class ServiceException extends RuntimeException {
    public ServiceException() {
        super("Internal Error");
    }
}
