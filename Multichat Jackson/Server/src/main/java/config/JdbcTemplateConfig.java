package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public final class JdbcTemplateConfig {
    private static JdbcTemplateConfig jdbcTemplateConfig;
    public static String s;

    private String dbUrl;
    private String id;
    private String pass;
    private String driverName;
    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;
    private JdbcTemplate jdbcTemplate;

    private JdbcTemplateConfig() {
        this.driverName = "org.postgresql.Driver";
        this.dbUrl = "jdbc:postgresql://localhost:5432/java_lab";
        this.id = "postgres";
        this.pass = "elvin2901";
        createJdbcTemplate();
    }

    private void createJdbcTemplate() {
        checkDriver();
        hikariConfig = new HikariConfig();
        hikariConfig.setUsername(id);
        hikariConfig.setPassword(pass);
        hikariConfig.setJdbcUrl(dbUrl);
        hikariDataSource = new HikariDataSource(hikariConfig);
        jdbcTemplate = new JdbcTemplate(hikariDataSource);
    }

    private void checkDriver() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static JdbcTemplateConfig getInstance() {
        if (jdbcTemplateConfig == null) {
            jdbcTemplateConfig = new JdbcTemplateConfig();
        }
        return jdbcTemplateConfig;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
