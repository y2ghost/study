package study.ywork.spring.test.profile;

import java.util.List;

public enum DataServiceRemote implements DataService {
    INSTANCE;

    @Override
    public List<Customer> getCustomersByAge(int minAge, int maxAge) {
        throw new UnsupportedOperationException("未实现");
    }
}
