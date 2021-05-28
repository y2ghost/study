package study.ywork.spring.provider;

public class MsgBean {
    private String msg;

    public MsgBean(String msg) {
        this.msg = msg;
    }

    public void showMessage() {
        System.out.println("消息: " + msg);
    }

    @Override
    public String toString() {
        return "MsgBean{" + "msg='" + msg + '\'' + '}';
    }
}
