package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ATUserDetailsService;
import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;

@Controller
public class DataController {
    @Autowired
    private ProjectService projectService;
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal ATUserDetailsService.ATUserDetails details) throws SQLException {
        model.addAttribute("interestedProjects", projectService.getUserInterestedProjects(details));
        return "home";
    }
}
