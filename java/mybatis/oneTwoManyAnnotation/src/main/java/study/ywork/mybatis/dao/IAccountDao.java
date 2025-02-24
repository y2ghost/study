package study.ywork.mybatis.dao;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import study.ywork.mybatis.domain.Account;
import java.util.List;

public interface IAccountDao {
    @Select("select * from account")
    @Results(id = "accountMap", value = { @Result(id = true, column = "id", property = "id"),
        @Result(column = "uid", property = "uid"), @Result(column = "money", property = "money"),
        @Result(property = "user", column = "uid", one = @One(select = "study.ywork.mybatis.dao.IUserDao.findById", fetchType = FetchType.EAGER)) })
    List<Account> findAll();
    @Select("select * from account where uid = #{userId}")
    List<Account> findAccountByUid(Integer userId);
}
