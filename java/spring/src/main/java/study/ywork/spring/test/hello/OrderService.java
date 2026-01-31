package study.ywork.spring.test.hello;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {
    public String placeOrders(List<Order> orders) {
        return orders.size() + " 个订单已下";
    }
}