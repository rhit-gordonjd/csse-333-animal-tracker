package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

@Component
public class UploadDataService {
    @Autowired
    DataSource dataSource;

    public void createImage(String url, int uploaderID) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call CreateUserImage(@UploaderID = ?, @URL = ?)}")
        ) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, uploaderID);
            stmt.setString(3, url);

            stmt.executeUpdate();

            int status = stmt.getInt(1);

            if (status != 0) {
                throw new SQLException("Stored Procedure CreateUserImage returned " + status);
            }
        }
    }
}
