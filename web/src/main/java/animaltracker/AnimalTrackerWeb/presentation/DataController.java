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
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) throws SQLException {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            model.addAttribute("interestedProjects", List.of());
        } else {
            List<ProjectService.Project> interestedProjects = projectService.getUserInterestedProjects(currentUser);
            model.addAttribute("interestedProjects", interestedProjects);
        }

        return "home";
    }
}
