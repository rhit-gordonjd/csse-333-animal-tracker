package animaltracker.AnimalTrackerWeb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrganismDataService {
    @Autowired
    private DataSource dataSource;

    public List<OrganismDTO> getOrganismsForProject(int projectId) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (CallableStatement stmt = connection.prepareCall(
                    "{? = call GetProjectOrganisms(@ProjectID = ?)}")) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setInt(2, projectId);

                ResultSet resultSet = stmt.executeQuery();

                List<OrganismDTO> out = parseOrganisms(resultSet);

                int status = stmt.getInt(1);
                if (status != 0) {
                    throw new SQLException("Stored Procedure GetProject returned " + status);
                }

                return out;
            }
        }
    }

    private List<OrganismDTO> parseOrganisms(ResultSet rs) throws SQLException {
        List<OrganismDTO> results = new ArrayList<>();
        int idIndex = rs.findColumn("ID");
        int commonNameIndex = rs.findColumn("CommonName");
        int scientificNameIndex = rs.findColumn("ScientificName");
        int descriptionIndex = rs.findColumn("Description");
        int identificationInstructionsIndex = rs.findColumn("IdentificationInstructions");
        while (rs.next()) {
            results.add(new OrganismDTO(
                    rs.getInt(idIndex),
                    rs.getString(commonNameIndex),
                    rs.getString(scientificNameIndex),
                    rs.getString(descriptionIndex),
                    rs.getString(identificationInstructionsIndex)
            ));
        }
        return results;
    }

    public static class OrganismDTO {
        private final int id;
        private final String commonName;
        private final String scientificName;
        private final String description;
        private final String identificationInstructions;

        private OrganismDTO(int id, String commonName, String scientificName, String description, String identificationInstructions) {
            this.id = id;
            this.commonName = commonName;
            this.scientificName = scientificName;
            this.description = description;
            this.identificationInstructions = identificationInstructions;
        }

        public int getId() {
            return id;
        }

        public String getCommonName() {
            return commonName;
        }

        public String getScientificName() {
            return scientificName;
        }

        public String getDescription() {
            return description;
        }

        public String getIdentificationInstructions() {
            return identificationInstructions;
        }
    }
}
