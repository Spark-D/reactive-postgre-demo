package com.example.reactivepostgredemo.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
public class ApplicationDataBaseConfig extends AbstractR2dbcConfiguration {
    @Override
    public ConnectionFactory connectionFactory() {
        String url = "r2dbc:postgresql://localhost/postgres";
        ConnectionFactory connectionFactory = ConnectionFactories.get(url);
        return connectionFactory;
    }
}
