package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.OrganismDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganismService {
    @Autowired
    private OrganismDataService organismDataService;

    public List<Organism> getOrganismsForProject(int projectId) throws SQLException {
        return organismDataService.getOrganismsForProject(projectId)
                .stream()
                .map(Organism::new)
                .collect(Collectors.toList());
    }

    public static class Organism {
        private final int id;
        private final String commonName;
        private final String scientificName;
        private final String description;
        private final String identificationInstructions;

        private Organism(OrganismDataService.OrganismDTO organismDTO) {
            this.id = organismDTO.getId();
            this.commonName = organismDTO.getCommonName();
            this.scientificName = organismDTO.getScientificName();
            this.description = organismDTO.getDescription();
            this.identificationInstructions = organismDTO.getIdentificationInstructions();
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
