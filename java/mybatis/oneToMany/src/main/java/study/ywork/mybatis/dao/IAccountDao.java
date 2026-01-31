package study.ywork.mybatis.dao;

import java.util.List;
import study.ywork.mybatis.domain.Account;
import study.ywork.mybatis.domain.AccountUser;

public interface IAccountDao {
    List<Account> findAll();
    List<AccountUser> findAllAccount();
}
