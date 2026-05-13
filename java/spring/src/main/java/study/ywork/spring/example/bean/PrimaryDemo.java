package study.ywork.spring.example.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/*
 * 使用@Primary注解解决多个依赖对象存在时的唯一性问题
 */
@Configuration
public class PrimaryDemo {
    @Bean
    public OrderService orderService() {
        return new OrderService();
    }

    @Bean
    public Dao getOnwDao() {
        return new OneDaoImpl();
    }

    @Primary
    @Bean
    public Dao getTwoDao() {
        return new TwoDaoImpl();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PrimaryDemo.class);
        OrderService orderService = context.getBean(OrderService.class);
        orderService.placeOrder("122");
        context.close();
    }

    private static interface Dao {
        void saveOrder(String orderId);
    }

    private static class OneDaoImpl implements Dao {
        @Override
        public void saveOrder(String orderId) {
            System.out.println("OneDao 订单保存 " + orderId);
        }
    }

    private static class TwoDaoImpl implements Dao {
        @Override
        public void saveOrder(String orderId) {
            System.out.println("TwoDao 订单保存 " + orderId);
        }
    }

    private static class OrderService {
        private Dao dao;

        public void placeOrder(String orderId) {
            System.out.println("下单 " + orderId);
            dao.saveOrder(orderId);
        }

        @Autowired
        public void setDao(Dao dao) {
            this.dao = dao;
        }
    }
}
