package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.SightingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
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

    public List<ProjectSightingWithProject> getUserSightings(User user, String sortType, String sortOrder) throws SQLException {
//        return sightingDataService.getUserSightings(user.getId(), )
//                .stream()
//                .map(ProjectSightingWithProject::new)
//                .collect(Collectors.toList());
        if (sortType.equals("Date")) {
            return getUserSightingsByTimestamp(user, sortOrder);
        } else if (sortType.equals("Organism")) {
            return getUserSightingsByOrganism(user, sortOrder);
        } else {
            return getUserSightingsByProject(user, sortOrder);
        }
    }

    private List<ProjectSightingWithProject> getUserSightingsByTimestamp(User user, String sortOrder) throws SQLException {
        List<SightingDataService.ProjectSightingWithProjectDTO> userSightings = sightingDataService.getUserSightingsByTime(user.getId(), sortOrder);
        if (userSightings != null) {
            return userSightings
                    .stream()
                    .map(ProjectSightingWithProject::new)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<ProjectSightingWithProject>();
        }


//        return sightingDataService.getUserSightingsByTime(user.getId(), sortOrder)
//                .stream()
//                .map(ProjectSightingWithProject::new)
//                .collect(Collectors.toList());
    }

    private List<ProjectSightingWithProject> getUserSightingsByOrganism(User user, String sortOrder) throws SQLException {
        return sightingDataService.getUserSightingsByOrganism(user.getId(), sortOrder)
                .stream()
                .map(ProjectSightingWithProject::new)
                .collect(Collectors.toList());
    }

    private List<ProjectSightingWithProject> getUserSightingsByProject(User user, String sortOrder) throws SQLException {
        return sightingDataService.getUserSightingsByProject(user.getId(), sortOrder)
                .stream()
                .map(ProjectSightingWithProject::new)
                .collect(Collectors.toList());
    }

    public ProjectSightingAndImages getSighting(int id) throws SQLException {
        SightingDataService.ProjectSightingAndImagesDTO dto = sightingDataService.getSighting(id);
        if (dto == null) {
            return null;
        }

        return new ProjectSightingAndImages(
                new ProjectSightingWithProject(dto.getDetails()),
                dto.getImages()
        );
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

    public static class ProjectSightingWithProject extends ProjectSighting {
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

    public static class ProjectSightingAndImages {
        private final ProjectSightingWithProject details;
        private final List<String> images;

        public ProjectSightingAndImages(ProjectSightingWithProject details, List<String> images) {
            this.details = details;
            this.images = images;
        }

        public ProjectSightingWithProject getDetails() {
            return details;
        }

        public List<String> getImages() {
            return images;
        }
    }
}
