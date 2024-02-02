package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.SettingsService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import animaltracker.AnimalTrackerWeb.data.UserSettingsService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Controller
public class UpdateDisplayNameController {

    @Autowired
    private SettingsService settings;

    @Autowired
    private UserService userService;

    @GetMapping("/updateDisplayName")
    public String updateDisplayNameForm(Model model) {
        model.addAttribute("newdisplayname", new UpdateDisplayNameForm());
        return "updateDisplayName";
    }

    @PostMapping("/updateDisplayName")
    public String updateDisplayName(Model model, @ModelAttribute("newdisplayname") @Valid UpdateDisplayNameForm newDisplayName) {
        User user = userService.getCurrentUser();
        int userID = user.getId();
        settings.updateDisplayName(userID, newDisplayName.getDisplayName());
        return "redirect:/settings";
    }

    public static class UpdateDisplayNameForm {
        @NotEmpty
        @Length(max = 50)
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }
    }

}
