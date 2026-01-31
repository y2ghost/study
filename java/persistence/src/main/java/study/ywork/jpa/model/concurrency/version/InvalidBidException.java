package study.ywork.jpa.model.concurrency.version;

public class InvalidBidException extends Exception {
    public InvalidBidException(String message) {
        super(message);
    }
}
