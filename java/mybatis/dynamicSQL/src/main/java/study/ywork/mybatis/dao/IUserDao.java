package study.ywork.mybatis.dao;

import java.util.List;
import study.ywork.mybatis.domain.QueryVo;
import study.ywork.mybatis.domain.User;

public interface IUserDao {
    List<User> findAll();
    User findById(Integer userId);
    List<User> findByName(String username);
    List<User> findUserByVo(QueryVo vo);
    List<User> findUserByCondition(User user);
    List<User> findUserInIds(QueryVo vo);
}
