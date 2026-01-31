package study.ywork.mybatis.dao;

import java.util.List;
import study.ywork.mybatis.dao.domain.User;

public interface IUserDao {
    List<User> findAll();
}