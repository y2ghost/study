package study.ywork.spring.test.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(HelloConfig.class)
class ShoppingCartTest {

    @Autowired
    private ShoppingCart shoppingCart;

    @Test
    void testCheckout() {
        shoppingCart.addItem("Item1", 3);
        shoppingCart.addItem("item2", 5);
        String result = shoppingCart.checkout();
        assertEquals("2 个订单已下", result);
    }
}