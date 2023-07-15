package study.ywork.spring.test.profile;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface DataService {
    List<Customer> getCustomersByAge(int minAge, int maxAge);
}