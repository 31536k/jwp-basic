package next.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import next.model.User;

public class UserDao {
    public void insert(final User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(final User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        List<User> result = jdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                null,
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"))
        );
        return result;
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        User result = jdbcTemplate.queryForObject("SELECT userId, password, name, emil FROM USERS WHERE userid=?",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pstmt) throws SQLException {
                        pstmt.setString(1, userId);
                    }
                }, rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
        return result;
    }
}
