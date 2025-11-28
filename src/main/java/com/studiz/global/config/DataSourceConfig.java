package com.studiz.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * DataSource 설정 클래스
 * Render에서 제공하는 postgresql:// 형식의 connectionString을 무시하고
 * 항상 jdbc:postgresql:// 형식으로 구성합니다.
 */
@Configuration
public class DataSourceConfig {

    @Value("${DB_HOST:localhost}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private String dbPort;

    @Value("${DB_NAME:studiz}")
    private String dbName;

    @Value("${DB_USERNAME:studiz}")
    private String dbUsername;

    @Value("${DB_PASSWORD:studiz}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        // 항상 jdbc:postgresql:// 형식으로 URL 구성
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName("org.postgresql.Driver");
        
        // Connection pool 설정 (배포 안정성을 위해 조정)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(60000);  // 60초로 증가
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setInitializationFailTimeout(-1);  // 연결 실패 시에도 계속 시도
        
        return new HikariDataSource(config);
    }
}

