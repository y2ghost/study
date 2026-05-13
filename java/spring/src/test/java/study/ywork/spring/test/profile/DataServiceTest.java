package study.ywork.spring.test.profile;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ProfileConfig.class)
@ActiveProfiles(ProfileConfig.PROFILE_LOCAL)
class DataServiceTest {
    @Autowired
    private DataService dataService;

    @Test
    void testCustomerAges() {
        List<Customer> customers = dataService.getCustomersByAge(25, 40);
        for (Customer customer : customers) {
            int age = customer.getAge();
            assertTrue(age >= 25 && age < 40, "年龄不在范围[25, 40): " + customer);
        }
    }
}
