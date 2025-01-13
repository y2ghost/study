package study.ywork.users.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import study.ywork.users.domain.UserRecord;
import study.ywork.users.repository.mapper.UserRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;

@Repository
public class UserJdbcRepository implements SimpleRepository<UserRecord, Long> {
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserRecord save(UserRecord user) {
        String sql = "INSERT INTO users_user (name, email, password, user_role, active) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            String[] array = user.userRole().stream().map(Enum::name).toArray(String[]::new);
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setString(3, user.password());
            ps.setArray(4, connection.createArrayOf("varchar", array));
            ps.setBoolean(5, user.active());
            return ps;
        }, keyHolder);

        return user.withId((Long) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<UserRecord> findById(Long id) {
        String sql = "SELECT * FROM users_user WHERE id = ?";
        Object[] params = new Object[]{id};
        UserRecord user = jdbcTemplate.queryForObject(sql, params, new int[]{Types.INTEGER}, new UserRowMapper());
        return Optional.ofNullable(user);
    }

    @Override
    public Iterable<UserRecord> findAll() {
        String sql = "SELECT * FROM users_user";
        return this.jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users_user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
