package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, PreparedStatementSetter pstmtSetter) throws DataAccessException {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void update(String sql, Object ... values) {
        PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(values);
        update(sql, preparedStatementSetter);
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object ... values) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (int i = 0; i < values.length; i++) {
                    pstmt.setObject(i+1, values[i]);
                }
            }
        };
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) throws DataAccessException {
        ResultSet rs = null;
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            if (pstmtSetter != null) {
                pstmtSetter.setValues(pstmt);
            }

            rs = pstmt.executeQuery();
            List<T> objects = new ArrayList<>();
            if (rs.next()) {
                objects.add(rowMapper.mapRow(rs));
            }
            return objects;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) throws DataAccessException {
        List<T> result = query(sql, pstmtSetter, rowMapper);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
