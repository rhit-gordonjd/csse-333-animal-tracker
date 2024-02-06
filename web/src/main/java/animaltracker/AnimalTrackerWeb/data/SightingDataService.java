package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public List<ProjectSightingDTO> getSightingByProject(int projectId) throws SQLException
    {
        try (Connection connection = dataSource.getConnection()) {
            try (CallableStatement stmt = connection.prepareCall(
                    "{? = call GetProjectSightings(@ProjectID = ?)}")) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setInt(2, projectId);

                ResultSet resultSet = stmt.executeQuery();

                List<SightingDataService.ProjectSightingDTO> out = parseProjectSightings(resultSet);

                int status = stmt.getInt(1);
                if (status != 0) {
                    throw new SQLException("Stored Procedure GetProject returned " + status);
                }

                if (out.isEmpty()) {
                    return null;
                } else {
                    return out;
                }
            }
        }
    }

    private List<SightingDataService.ProjectSightingDTO> parseProjectSightings(ResultSet rs) throws SQLException
    {
        List<SightingDataService.ProjectSightingDTO> results = new ArrayList<>();
        int idIndex = rs.findColumn("ID");
        int timestampIndex = rs.findColumn("Timestamp");
        int locationLatitudeIndex = rs.findColumn("LocationLatitude");
        int locationLongitudeIndex = rs.findColumn("LocationLongitude");
        int commonNameIndex = rs.findColumn("CommonName");
        int scientificNameIndex = rs.findColumn("ScientificName");
        int displayNameIndex = rs.findColumn("DisplayName");
        while (rs.next()) {
            results.add(new SightingDataService.ProjectSightingDTO(rs.getInt(idIndex), rs.getTimestamp(timestampIndex), rs.getFloat(locationLatitudeIndex),
                    rs.getFloat(locationLongitudeIndex), rs.getString(commonNameIndex), rs.getString(scientificNameIndex), rs.getString(displayNameIndex)));
        }
        return results;
    }

    public static class ProjectSightingDTO {
        private final int id;
        private final Timestamp timestamp;
        private final float latitude;
        private final float longitude;
        private final String commonName;
        private final String scientificName;
        private final String displayName;


        public ProjectSightingDTO(int id, Timestamp timestamp, float latitude, float longitude, String commonName, String scientificName, String displayName) {
            this.id = id;
            this.timestamp = timestamp;
            this.latitude = latitude;
            this.longitude = longitude;
            this.commonName = commonName;
            this.scientificName = scientificName;
            this.displayName = displayName;
        }

        public int getId() {
            return id;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public float getLongitude() {
            return longitude;
        }

        public String getCommonName() {
            return commonName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getScientificName() {
            return scientificName;
        }

        public float getLatitude() {
            return latitude;
        }
    }
}
