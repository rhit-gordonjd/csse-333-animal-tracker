package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects/{projectId}")
    public String projectById(@PathVariable int projectId, Model model) throws SQLException {

        ProjectService.Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);

        return "project";
    }
}
