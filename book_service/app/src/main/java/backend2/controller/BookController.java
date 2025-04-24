package backend2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
public class BookController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String testDatabase() {
        try {
            // Test database connection by running a simple query
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return "Database connection successful!";
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
} 