package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.SettingsService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import animaltracker.AnimalTrackerWeb.data.UserSettingsService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Controller
public class UpdateUsernameController {

    @Autowired
    private SettingsService settings;

    @Autowired
    private UserService userService;

    @GetMapping("/settings/updateusername")
    public String updateUsername(Model model) {
        model.addAttribute("usernameForm", new UpdateUsernameForm());
        return "update_username";
    }

    @PostMapping("/settings/updateusername")
    public String updateUsername(@Valid @ModelAttribute("usernameForm") UpdateUsernameForm usernameForm, BindingResult bindingResult, Model model) {
        model.addAttribute("usernameForm", usernameForm);
        User user = userService.getCurrentUser();
        int userID = user.getId();

        try{
            settings.updateUsername(userID, usernameForm.getnewusername());
        }catch(Exception e){
            System.out.println("Something went wrong");
        }
        userService.reloadUser();
        return "redirect:/settings";
    }


    public static class UpdateUsernameForm {
        @NotEmpty
        @Length(max = 50)
        private String newusername;

        public String getnewusername() {
            return this.newusername;
        }

        public void setnewusername(String newusername) {
            this.newusername = newusername;
        }
    }

}
