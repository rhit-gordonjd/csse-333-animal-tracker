package animaltracker.AnimalTrackerWeb.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String settings() {
        return "user_settings";
    }


    @PostMapping("/updatedisplayname")
    public String displayName() {
        return "update_display_name";
    }

    @PostMapping("/updateusername")
    public String userame() {
        return "update_username";
    }

    @PostMapping("/updatepassword")
    public String password() {
        return "update_password";
    }


}
