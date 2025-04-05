package kg.attractor.payment_system.dao;

import kg.attractor.payment_system.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public void save(User user) {
        Long roleId = getRoleIdByName("APPLICANT");

        String sql = "INSERT INTO users (phone_number, username, password, role_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getPhoneNumber(), user.getUsername(), user.getPassword(), roleId);
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(1) FROM users WHERE phone_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone);
        return count != null && count > 0;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(1) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public User findByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone_number = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new User(
                        rs.getLong("id"),
                        rs.getString("phone_number"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getLong("role_id"),
                        rs.getBoolean("enabled")
                ), phone);
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new User(
                        rs.getLong("id"),
                        rs.getString("phone_number"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getLong("role_id"),
                        rs.getBoolean("enabled")
                ), id);
    }

    public Long getRoleIdByName(String roleName) {
        String sql = "SELECT id FROM roles WHERE role = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, roleName);
    }
}
