package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectDataService {
    @Autowired
    private DataSource dataSource;


    public List<ProjectDTO> getInterestedProjects(int userId) throws SQLException
    {
        try (Connection connection = dataSource.getConnection()) {
            try (CallableStatement stmt = connection.prepareCall(
                    "{? = call RetrieveUserInterestedProjects(@UserID = ?)}")) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setInt(2, userId);

                ResultSet resultSet = stmt.executeQuery();

                List<ProjectDTO> out = parseProjects(resultSet);

                int status = stmt.getInt(1);
                if (status != 0) {
                    throw new SQLException("Stored Procedure ImportProject returned " + status);
                }

                return out;
            }
        }
    }

    private List<ProjectDTO> parseProjects(ResultSet rs) throws SQLException
    {
        List<ProjectDTO> results = new ArrayList<>();
        int idIndex = rs.findColumn("ID");
        int nameIndex = rs.findColumn("Name");
        int descriptionIndex = rs.findColumn("Description");
        int creationTimestampIndex = rs.findColumn("CreationTimestamp");
        int closedDateIndex = rs.findColumn("ClosedDate");
        while (rs.next()) {
            results.add(new ProjectDTO(rs.getInt(idIndex), rs.getString(nameIndex), rs.getString(descriptionIndex),
                    rs.getTimestamp(creationTimestampIndex), rs.getTimestamp(closedDateIndex)));
        }
        return results;
    }

    public static class ProjectDTO {
        private final int id;
        private final String name;
        private final String description;
        private final Timestamp creationTimestamp;
        private final Timestamp closedDate;

        public ProjectDTO(int id, String name, String description, Timestamp creationTimestamp, Timestamp closedDate) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.creationTimestamp = creationTimestamp;
            this.closedDate = closedDate;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Timestamp getCreationTimestamp() {
            return creationTimestamp;
        }

        public Timestamp getClosedDate() {
            return closedDate;
        }
    }
}
