package com.tasklink.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusConstraintMigrationRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(OrderStatusConstraintMigrationRunner.class);
    private final JdbcTemplate jdbcTemplate;

    public OrderStatusConstraintMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("""
                ALTER TABLE task_order
                DROP CONSTRAINT IF EXISTS task_order_status_check
                """);

        jdbcTemplate.execute("""
                ALTER TABLE task_order
                ADD CONSTRAINT task_order_status_check
                CHECK (status IN ('OPEN', 'IN_PROGRESS', 'COMPLETED', 'DRAFT', 'PUBLISHED', 'CLOSED'))
                """);

        log.info("Ensured task_order_status_check allows current OrderStatus values");
    }
}
