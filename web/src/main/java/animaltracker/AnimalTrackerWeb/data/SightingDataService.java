package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;

@Component
public class SightingDataService {
    @Autowired
    private DataSource dataSource;

    public int createSighting(int userId, int organismId, Instant timestamp, double latitude, double longitude, String imageURL) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call CreateSighting(@UserID = ?, @OrganismID = ?, @Timestamp = ?, @Latitude = ?, @Longitude = ?, @ImageURL = ?, @SightingID = ?)}")
        ) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, userId);
            stmt.setInt(3, organismId);
            stmt.setTimestamp(4, Timestamp.from(timestamp));
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, longitude);
            stmt.setString(7, imageURL);
            stmt.registerOutParameter(8, Types.INTEGER);

            stmt.executeUpdate();

            int status = stmt.getInt(1);

            if (status != 0) {
                throw new SQLException("Stored Procedure CreateSighting returned " + status);
            }

            return stmt.getInt(8);
        }
    }
}
