package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import animaltracker.AnimalTrackerWeb.logic.SightingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SightingService sightingService;

    @GetMapping("/projects/{projectId}")
    public String projectById(@PathVariable int projectId, Model model) throws SQLException {

        ProjectService.Project project = projectService.getProjectById(projectId);
        List<SightingService.ProjectSighting> projectSighting = sightingService.getProjectSightings(projectId);

        model.addAttribute("project", project);
        model.addAttribute("projectSightings", projectSighting);

        return "project";
    }

    @PostMapping("/projects/{projectId}/close")
    public String closeProject(@PathVariable int projectId) throws SQLException {
        ProjectService.Project project = projectService.getProjectById(projectId);

        if (!project.isOwner()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        projectService.closeProject(project);

        return "redirect:/projects/{projectId}";
    }
}
