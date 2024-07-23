package study.ywork.upload.service.dto;

public class ResultDTO<T> {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    private int status;
    private String message;
    private T data;

    public ResultDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultDTO<T> ok(T data) {
        return new ResultDTO<>(SUCCESS, "success", data);
    }

    public static <T> ResultDTO<T> error(String msg) {
        return new ResultDTO<>(FAIL, msg, null);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
