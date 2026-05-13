package study.ywork.spring.provider;

import org.springframework.core.Ordered;

public class OrderMsgBean implements Ordered {
    private String msg;
    private int order;

    public OrderMsgBean(String msg, int order) {
        this.msg = msg;
        this.order = order;
    }

    public void showMessage() {
        System.out.println("消息: " + msg);
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "OrderMsgBean [msg=" + msg + ", order=" + order + "]";
    }
}
