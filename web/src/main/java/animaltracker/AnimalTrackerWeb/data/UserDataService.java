package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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
