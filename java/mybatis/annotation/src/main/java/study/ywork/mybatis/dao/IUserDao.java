package study.ywork.mybatis.dao;

import org.apache.ibatis.annotations.Select;
import study.ywork.mybatis.domain.User;
import java.util.List;

public interface IUserDao {
    @Select("select * from user")
    List<User> findAll();
}
