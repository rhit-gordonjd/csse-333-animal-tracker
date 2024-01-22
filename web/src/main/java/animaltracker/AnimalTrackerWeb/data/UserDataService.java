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

    public UserDTO getUserByID(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            throw new RuntimeException("Not Implemented");
        }
    }

    public UserDTO getUserByUsername(String username) throws SQLException {
//        try (Connection connection = dataSource.getConnection()) {
//            throw new RuntimeException("Not Implemented");
//        }

        if (username.equals("user")) {
            return new UserDTO(1, "user", "{noop}password");
        }

        return null;
    }

    public void createUser(String username, String encodedPassword) throws SQLException {
//        try (Connection connection = dataSource.getConnection()) {
//            throw new RuntimeException("Not Implemented");
//        }
    }

    public static class UserDTO {
        private final int id;
        private final String username;
        private final String encodedPassword;

        UserDTO(int id, String username, String encodedPassword) {
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
