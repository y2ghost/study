package study.ywork.mybatis.dao;

import java.util.List;
import study.ywork.mybatis.domain.User;

public interface IUserDao {
    List<User> findAll();
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Integer userId);
    User findById(Integer userId);
    List<User> findByName(String username);
    int findTotal();
}
