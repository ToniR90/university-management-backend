package com.orientation.backend.shared.infrastructure.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Custom health indicator for database connection monitoring.
 * Provides detailed information about PostgreSQL connection status.
 */

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health(){
        try(Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return Health.up()
                        .withDetail("database", connection.getMetaData().getDatabaseProductName())
                        .withDetail("version", connection.getMetaData().getDatabaseProductVersion())
                        .withDetail("driver", connection.getMetaData().getDriverName())
                        .withDetail("schema", connection.getSchema())
                        .build();
            } else {
                return Health.down().withDetail("error", "Connection is not valid").build();
            }
        } catch(SQLException e){
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

}
