package study.ywork.spring.bean.qualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import study.ywork.spring.bean.qualifier.service.OrderService;
import java.util.Arrays;
import javax.annotation.Resource;

public class OrderServiceClient {
    // 指定注入的BEAN名称，@Resource注解等同与@Autowired+@Qualifier
    @Autowired
    @Qualifier("orderServiceOne")
    private OrderService orderServiceOne;
    @Resource(name = "orderServiceTwo")
    private OrderService orderServiceTwo;

    public void showPendingOrderDetails() {
        for (String orderId : Arrays.asList("100", "200", "300")) {
            System.out.println(orderServiceOne.getOrderDetails(orderId));
            System.out.println(orderServiceTwo.getOrderDetails(orderId));
        }
    }
}
