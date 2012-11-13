package org.inigma.shared.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Dynamically loads and reloads configuration settings from a collection. The data model is presumed to be in the
 * following syntax.
 * 
 * <pre>
 * CREATE TABLE config ( id VARCHAR(100) NOT NULL, value VARCHAR(4000), PRIMARY KEY(id) );
 * </pre>
 */
public class JdbcConfiguration extends AbstractConfiguration {
    private static class JdbcConfigurationTemplate extends JdbcTemplate {
        private static final String SQL_SELECT = "SELECT value FROM config WHERE id=?";
        private static final String SQL_UPDATE = "UPDATE config SET value=? WHERE id=?";
        private static final String SQL_DELETE = "DELETE FROM config WHERE id=?";

        @Autowired
        public JdbcConfigurationTemplate(DataSource ds) {
            super(ds);
        }

        public <T> T findById(String id, Class<T> type) {
            try {
                return queryForObject(SQL_SELECT, type, id);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }

        public int removeById(String id) {
            return update(SQL_DELETE, id);
        }

        public int updateById(String id, Object value) {
            return update(SQL_UPDATE, value, id);
        }
    }

    private JdbcConfigurationTemplate configTemplate;

    @Autowired
    public JdbcConfiguration(DataSource ds) {
        this.configTemplate = new JdbcConfigurationTemplate(ds);
    }

    @Override
    protected <T> T getValue(String key, Class<T> type) {
        return configTemplate.findById(key, type);
    }

    @Override
    protected void removeValue(String key) {
        configTemplate.removeById(key);
    }

    @Override
    protected void setValue(String key, Object value) {
        configTemplate.updateById(key, value);
    }
}
