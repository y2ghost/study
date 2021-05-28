package study.ywork.mybatis.dao;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.mapping.FetchType;
import study.ywork.mybatis.domain.User;
import java.util.List;

@CacheNamespace(blocking = true)
public interface IUserDao {
    @Select("select * from user")
    @Results(id = "userMap", value = { @Result(id = true, column = "id", property = "userId"),
        @Result(column = "username", property = "userName"), @Result(column = "address", property = "userAddress"),
        @Result(column = "sex", property = "userSex"), @Result(column = "birthday", property = "userBirthday"),
        @Result(property = "accounts", column = "id", many = @Many(select = "study.ywork.mybatis.dao.IAccountDao.findAccountByUid", fetchType = FetchType.LAZY)) })
    List<User> findAll();

    @Select("select * from user  where id=#{id} ")
    @ResultMap("userMap")
    User findById(Integer userId);

    @Select("select * from user where username like #{userName} ")
    @ResultMap("userMap")
    List<User> findUserByName(String username);
}
