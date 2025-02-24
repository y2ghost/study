package study.ywork.users.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import study.ywork.users.domain.UserRecord;
import study.ywork.users.domain.UserRole;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserRowMapper implements RowMapper<UserRecord> {
    @Override
    public UserRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array array = rs.getArray("user_role");
        String[] rolesArray = Arrays.copyOf((Object[]) array.getArray(), ((Object[]) array.getArray()).length, String[].class);
        List<UserRole> roles = Arrays.stream(rolesArray).map(UserRole::valueOf).toList();
        return UserRecord.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .userRole(roles)
                .build();
    }
}
