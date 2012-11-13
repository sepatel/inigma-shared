package org.inigma.shared.jdbc;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class NullableJdbcTemplate extends JdbcTemplate {
    public NullableJdbcTemplate() {
    }

    public NullableJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public NullableJdbcTemplate(DataSource dataSource, boolean lazyInit) {
        super(dataSource, lazyInit);
    }

    @Override
    public <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse)
            throws DataAccessException {
        try {
            return super.query(psc, pss, rse);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
            throws DataAccessException {
        try {
            return super.queryForObject(sql, args, argTypes, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Override
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            return super.queryForObject(sql, args, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return queryForObject(sql, args, rowMapper);
    }
    
    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return queryForObject(sql, null, rowMapper);
    }
}
