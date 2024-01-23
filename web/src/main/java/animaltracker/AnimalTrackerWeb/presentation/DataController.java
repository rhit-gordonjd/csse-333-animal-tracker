package animaltracker.AnimalTrackerWeb.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DataController {
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
