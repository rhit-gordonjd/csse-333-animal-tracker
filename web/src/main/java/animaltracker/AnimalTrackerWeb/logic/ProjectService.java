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
    @Autowired
    private UserService userService;

    private Integer getCurrentUserIdOrNull() {
        User user = userService.getCurrentUser();
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }

    public List<Project> getUserInterestedProjects() throws SQLException {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }

        List<ProjectDataService.ProjectDTO> interestedProjects = projectDataService.getInterestedProjects(currentUser.getId());
        return interestedProjects
                .stream()
                .map(Project::new)
                .collect(Collectors.toList());
    }

    public Project getProjectById(int id) throws SQLException {
        ProjectDataService.ProjectDTO projectDTO = projectDataService.getProjectById(getCurrentUserIdOrNull(), id);
        if (projectDTO == null) {
            return null;
        } else {
            return new Project(projectDTO);
        }
    }

    public List<Project> getAllProjects() throws SQLException {
        List<ProjectDataService.ProjectDTO> interestedProjects = projectDataService.getAllProjects(getCurrentUserIdOrNull());
        return interestedProjects
                .stream()
                .map(Project::new)
                .collect(Collectors.toList());
    }

    public void setInterested(User user, int projectId, boolean interested) throws SQLException {
        projectDataService.setInterested(user.getId(), projectId, interested);
    }

    public static class Project {
        private final int id;
        private final String name;
        private final String description;
        private final Instant creationTimestamp;
        private final Instant closedDate;
        private final boolean isInterested;
        private final boolean isMember;
        private final boolean isOwner;

        public Project(ProjectDataService.ProjectDTO projectDTO) {
            this.id = projectDTO.getId();
            this.name = projectDTO.getName();
            this.description = projectDTO.getDescription();
            this.creationTimestamp = projectDTO.getCreationTimestamp().toInstant();
            this.closedDate = (projectDTO.getClosedDate() == null ? null : projectDTO.getClosedDate().toInstant());
            this.isInterested = projectDTO.isInterested();
            this.isMember = projectDTO.isMember();
            this.isOwner = projectDTO.isOwner();
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