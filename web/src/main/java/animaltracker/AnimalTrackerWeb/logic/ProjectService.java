package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.ProjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectService {
    @Autowired
    private ProjectDataService projectDataService;

    public List<Project> getUserInterestedProjects(User details) throws SQLException {
        int userId = details.getId();
        List<ProjectDataService.ProjectDTO> interestedProjects = projectDataService.getInterestedProjects(userId);
        return interestedProjects
                .stream()
                .map(Project::new)
                .collect(Collectors.toList());
    }

    public Project getProjectById(int id) throws SQLException {
        ProjectDataService.ProjectDTO projectDTO = projectDataService.getProjectById(id);
        if (projectDTO == null) {
            return null;
        } else {
            return new Project(projectDTO);
        }
    }

    public static class Project {
        private final int id;
        private final String name;
        private final String description;
        private final Instant creationTimestamp;
        private final Instant closedDate;

        public Project(ProjectDataService.ProjectDTO projectDTO) {
            this.id = projectDTO.getId();
            this.name = projectDTO.getName();
            this.description = projectDTO.getDescription();
            this.creationTimestamp = projectDTO.getCreationTimestamp().toInstant();
            this.closedDate = (projectDTO.getClosedDate() == null ? null : projectDTO.getClosedDate().toInstant());
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

        public Instant getCreationTimestamp() {
            return creationTimestamp;
        }

        public Instant getClosedDate() {
            return closedDate;
        }

        public boolean isCurrentlyClosed() {
            return closedDate != null && closedDate.isBefore(Instant.now());
        }
    }
}