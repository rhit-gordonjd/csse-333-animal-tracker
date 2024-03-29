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


    public List<ProjectDTO> getInterestedProjects(int userId) throws SQLException {
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

    public List<ProjectDTO> getAllProjects(Integer userId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (CallableStatement stmt = connection.prepareCall(
                    "{? = call GetAllProjects(@UserID = ?)}")) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setObject(2, userId, Types.INTEGER);

                ResultSet resultSet = stmt.executeQuery();

                List<ProjectDTO> out = parseProjects(resultSet);

                int status = stmt.getInt(1);
                if (status != 0) {
                    throw new SQLException("Stored Procedure GetAllProjects returned " + status);
                }

                return out;
            }
        }
    }

    public ProjectDTO getProjectById(Integer userId, int id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (CallableStatement stmt = connection.prepareCall(
                    "{? = call GetProject(@UserID = ?, @ProjectID = ?)}")) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setObject(2, userId, Types.INTEGER);
                stmt.setInt(3, id);

                ResultSet resultSet = stmt.executeQuery();

                List<ProjectDTO> out = parseProjects(resultSet);

                int status = stmt.getInt(1);
                if (status != 0) {
                    throw new SQLException("Stored Procedure GetProject returned " + status);
                }

                if (out.isEmpty()) {
                    return null;
                } else {
                    return out.get(0);
                }
            }
        }
    }

    public void setInterested(int userId, int projectId, boolean interested) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call SetInterested(@UserID = ?, @ProjectID = ?, @IsInterested = ?)}")) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, userId);
            stmt.setInt(3, projectId);
            stmt.setBoolean(4, interested);

            stmt.executeUpdate();

            int status = stmt.getInt(1);
            if (status != 0) {
                throw new SQLException("Stored Procedure GetProject returned " + status);
            }
        }
    }

    private List<ProjectDTO> parseProjects(ResultSet rs) throws SQLException {
        List<ProjectDTO> results = new ArrayList<>();
        int idIndex = rs.findColumn("ID");
        int nameIndex = rs.findColumn("Name");
        int descriptionIndex = rs.findColumn("Description");
        int creationTimestampIndex = rs.findColumn("CreationTimestamp");
        int closedDateIndex = rs.findColumn("ClosedDate");
        int interestedIndex = rs.findColumn("IsInterested");
        int memberIndex = rs.findColumn("IsMember");
        int ownerIndex = rs.findColumn("IsOwner");
        while (rs.next()) {
            results.add(new ProjectDTO(
                    rs.getInt(idIndex),
                    rs.getString(nameIndex),
                    rs.getString(descriptionIndex),
                    rs.getTimestamp(creationTimestampIndex),
                    rs.getTimestamp(closedDateIndex),
                    rs.getBoolean(interestedIndex),
                    rs.getBoolean(memberIndex),
                    rs.getBoolean(ownerIndex)
            ));
        }
        return results;
    }

    public void closeProject(int projectId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "{? = call CloseProject(@ProjectID = ?)}")) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, projectId);

            stmt.executeUpdate();

            int status = stmt.getInt(1);
            if (status != 0) {
                throw new SQLException("Stored Procedure CloseProject returned " + status);
            }
        }
    }

    public static class ProjectDTO {
        private final int id;
        private final String name;
        private final String description;
        private final Timestamp creationTimestamp;
        private final Timestamp closedDate;
        private final boolean isInterested;
        private final boolean isMember;
        private final boolean isOwner;

        public ProjectDTO(int id, String name, String description, Timestamp creationTimestamp, Timestamp closedDate, boolean isInterested, boolean isMember, boolean isOwner) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.creationTimestamp = creationTimestamp;
            this.closedDate = closedDate;
            this.isInterested = isInterested;
            this.isMember = isMember;
            this.isOwner = isOwner;
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

        public boolean isInterested() {
            return isInterested;
        }

        public boolean isMember() {
            return isMember;
        }

        public boolean isOwner() {
            return isOwner;
        }
    }
}
