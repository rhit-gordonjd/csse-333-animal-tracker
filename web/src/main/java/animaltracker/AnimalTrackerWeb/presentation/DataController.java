package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class DataController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public String home(Model model) throws SQLException {
        List<ProjectService.Project> interestedProjects = projectService.getUserInterestedProjects();
        model.addAttribute("interestedProjects", interestedProjects);

        return "home";
    }

    @GetMapping("/projects")
    public String allProjects(Model model) throws SQLException {
        List<ProjectService.Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

        return "projects";
    }
}
