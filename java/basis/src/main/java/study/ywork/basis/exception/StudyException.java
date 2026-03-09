package study.ywork.basis.exception;

public class StudyException extends RuntimeException {
    public StudyException(Exception e) {
        super("学习的代码出异常咯 " + e.getMessage());
    }
}
