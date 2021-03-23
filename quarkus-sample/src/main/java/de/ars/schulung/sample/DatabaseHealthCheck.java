package de.ars.schulung.sample;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        // we make a simple DB call
        try {
            final Connection connection = dataSource.getConnection();
            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1")) {
                // access works
                return HealthCheckResponse.up("database");
            }
        } catch (SQLException e) {
            return HealthCheckResponse.down("database");
        }
    }
}
