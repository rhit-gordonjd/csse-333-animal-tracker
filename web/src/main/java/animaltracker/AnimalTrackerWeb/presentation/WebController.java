package animaltracker.AnimalTrackerWeb.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class WebController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/connection_info")
    public String connection_info() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return connection.toString();
        }
    }
}
