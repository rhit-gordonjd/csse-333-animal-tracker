package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import animaltracker.AnimalTrackerWeb.logic.SightingService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SightingService sightingService;

    @GetMapping("/project/{projectId}")
    public String projectById(@PathVariable int projectId, Model model) throws SQLException {

        ProjectService.Project project = projectService.getProjectById(projectId);
        List<SightingService.ProjectSighting> projectSighting = sightingService.getProjectSightings(projectId);

        model.addAttribute("project", project);
        model.addAttribute("projectSightings", projectSighting);

        return "project";
    }
}