package de.ars.schulung.samples.services.todo;

import de.ars.schulung.samples.services.todo.entity.TodosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;

@Configuration
public class ActuatorConfig {

    // -------------------------------------

    @Autowired
    JdbcTemplate db;

    // URL: .../actuator/health
    // we could also write a @Component annotated class
    @Bean
    public HealthIndicator databaseQueryWorks() {
        return () -> {
            try {
                // test query
                db.query("select 1", (rs, num) -> 1);
                return Health.up().build();
            } catch (DataAccessException ex) {
                // see application.yml for activation of details
                return Health.down().withDetail("cause", "database query does not work").withException(ex).build();
            }
        };
    }

    // -------------------------------------

    @Autowired
    TodosRepository todosRepository;

    // URL: .../actuator/info
    // further information: https://codeboje.de/spring-boot-info-actuator

    @Bean
    public InfoContributor provideTodosInfos() {
        return builder -> builder.withDetail("todos", new HashMap<String, Object>() {
            {
                // ab Java 9 einfacher: Map.of("","");
                put("count", todosRepository.count());
            }
        });
    }

    // -------------------------------------

    // URL: .../actuator/metrics
    // URL: .../actuator/metrics/system.cpu.usage
    // URL: .../actuator/prometheus

}