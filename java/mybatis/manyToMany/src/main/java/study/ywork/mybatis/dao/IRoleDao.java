package study.ywork.mybatis.dao;

import java.util.List;
import study.ywork.mybatis.domain.Role;

public interface IRoleDao {
    List<Role> findAll();
}
