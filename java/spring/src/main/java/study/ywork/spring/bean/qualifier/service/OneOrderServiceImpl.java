package study.ywork.spring.bean.qualifier.service;

public class OneOrderServiceImpl implements OrderService {
    public String getOrderDetails(String orderId) {
        return "订单类型1，显示订单信息用于id=" + orderId;
    }
}
