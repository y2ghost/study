package study.ywork.users.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import study.ywork.users.domain.User;

import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    // 自定义查询
    @Query("SELECT id FROM User WHERE email = :email")
    Long getIdByEmail(@Param("email") String email);
}
