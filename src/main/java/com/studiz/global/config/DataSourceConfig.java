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
 * Render에서 자동으로 설정하는 SPRING_DATASOURCE_URL(postgresql:// 형식)을 무시하고
 * 항상 jdbc:postgresql:// 형식으로 URL을 구성합니다.
 */
@Configuration
public class DataSourceConfig {

    @Value("${DB_HOST}")
    private String dbHost;

    @Value("${DB_PORT}")
    private String dbPort;

    @Value("${DB_NAME}")
    private String dbName;

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
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
        config.setConnectionTimeout(60000);
        config.setInitializationFailTimeout(-1);
        
        return new HikariDataSource(config);
    }
}





