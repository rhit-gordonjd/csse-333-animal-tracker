package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class UserDataService {
    @Autowired
    private DataSource dataSource;

    public User getUserByID(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            throw new RuntimeException("Not Implemented");
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            throw new RuntimeException("Not Implemented");
        }
    }

    public static class User {
        private final int id;
        private final String username;
        private final String encodedPassword;

        User(int id, String username, String encodedPassword) {
            this.id = id;
            this.username = username;
            this.encodedPassword = encodedPassword;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEncodedPassword() {
            return encodedPassword;
        }
    }
}
