package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class UserDataService {
    @Autowired
    private DataSource dataSource;

    public UserDTO getUserByUsername(String username) throws SQLException {
        UserDTO out = null;

        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call GetLoginInfo(@Username = ?)}")
        ) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, username);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                out = new UserDTO(
                        rs.getInt("ID"),
                        username,
                        rs.getString("DisplayName"),
                        rs.getString("PasswordHash")
                );
            }

            int status = stmt.getInt(1);

            if (status != 0) {
                throw new SQLException("Stored Procedure GetLoginInfo returned " + status);
            }

            return out;
        }
    }

    /**
     * Gets a user by their id
     * Note: does not get the user's password, use getUserByUsername for logging in
     */
    public UserDTO getUserById(int id) throws SQLException {
        UserDTO out = null;

        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call GetUserInfo(@ID = ?)}")
        ) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                out = new UserDTO(
                        id,
                        rs.getString("Username"),
                        rs.getString("DisplayName"),
                        rs.getString("PasswordHash")
                );
            }

            int status = stmt.getInt(1);

            if (status != 0) {
                throw new SQLException("Stored Procedure GetUserInfo returned " + status);
            }

            return out;
        }
    }

    public void createUser(String username, String displayName, String encodedPassword) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call CreateAccount(@Username = ?, @DisplayName = ?, @PasswordHash = ?, @ID = NULL)}")
        ) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, username);
            stmt.setString(3, displayName);
            stmt.setString(4, encodedPassword);

            stmt.executeUpdate();

            int status = stmt.getInt(1);

            if (status != 0) {
                throw new SQLException("Stored Procedure CreateAccount returned " + status);
            }
        }
    }

    public static class UserDTO {
        private final int id;
        private final String username;
        private final String displayName;
        private final String encodedPassword;

        UserDTO(int id, String username, String displayName, String encodedPassword) {
            this.id = id;
            this.username = username;
            this.displayName = displayName;
            this.encodedPassword = encodedPassword;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEncodedPassword() {
            return encodedPassword;
        }
    }
}
