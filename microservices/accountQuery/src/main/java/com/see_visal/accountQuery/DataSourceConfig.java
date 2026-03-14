package com.see_visal.accountQuery;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource pgDataSource(DataSourceProperties props) {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(props.getDriverClassName())
                .url(props.getUrl())
                .username(props.getUsername())
                .password(props.getPassword())
                .build();
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        return new HikariDataSource(config);
    }

}
