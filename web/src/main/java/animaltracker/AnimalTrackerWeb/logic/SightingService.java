package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.SightingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SightingService {
    @Autowired
    private SightingDataService sightingDataService;

    public int createSighting(User user, OrganismService.Organism organism, Instant timestamp, Location location, String imageURL) throws SQLException {
        return sightingDataService.createSighting(user.getId(), organism.getId(), timestamp, location.getLatitude(), location.getLongitude(), imageURL);
    }

    public List<ProjectSighting> getProjectSightings(int projectId) throws SQLException {
        return sightingDataService.getSightingByProject(projectId)
                .stream()
                .map(ProjectSighting::new)
                .collect(Collectors.toList());
    }

    public List<ProjectSightingWithProject> getUserSightings(User user) throws SQLException {
        return sightingDataService.getUserSightings(user.getId())
                .stream()
                .map(ProjectSightingWithProject::new)
                .collect(Collectors.toList());
    }


    public static class ProjectSighting {
        private final int id;
        private final Timestamp timestamp;
        private final float latitude;
        private final float longitude;
        private final String commonName;
        private final String scientificName;
        private final String displayName;

        public ProjectSighting(SightingDataService.ProjectSightingDTO projectSightingDTO) {
            this.id = projectSightingDTO.getId();
            this.timestamp = projectSightingDTO.getTimestamp();
            this.latitude = projectSightingDTO.getLatitude();
            this.longitude = projectSightingDTO.getLongitude();
            this.commonName = projectSightingDTO.getCommonName();
            this.scientificName = projectSightingDTO.getScientificName();
            this.displayName = projectSightingDTO.getDisplayName();
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

    public static class ProjectSightingWithProject extends ProjectSighting  {
        private final int projectId;
        private final String projectName;

        public ProjectSightingWithProject(SightingDataService.ProjectSightingWithProjectDTO projectSightingDTO) {
            super(projectSightingDTO);
            projectId = projectSightingDTO.getProjectId();
            projectName = projectSightingDTO.getProjectName();
        }

        public int getProjectId() {
            return projectId;
        }

        public String getProjectName() {
            return projectName;
        }
    }
}
