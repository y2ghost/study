package study.ywork.mybatis.dao;

import java.util.List;
import study.ywork.mybatis.domain.Account;

public interface IAccountDao {
    List<Account> findAll();
    List<Account> findAccountByUid(Integer uid);
}
