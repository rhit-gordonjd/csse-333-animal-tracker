package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.SettingsService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        if(bindingResult.hasErrors()) {
            return "update_username";
        }
        model.addAttribute("usernameForm", usernameForm);
        User user = userService.getCurrentUser();
        int userID = user.getId();

        try{
            if (!settings.updateUsername(userID, usernameForm.getnewusername())) {
                ObjectError updateError = new ObjectError("globalError", "something went wrong, please try again");
                bindingResult.addError(updateError);
                return "update_username";
            }
        }catch(Exception e){
            System.out.println("Something went wrong");
            ObjectError updateError = new ObjectError("globalError", "something went wrong, please try again");
            bindingResult.addError(updateError);
            return "update_username";
        }
        return "redirect:/settings";
    }


    public static class UpdateUsernameForm {
        @NotEmpty(message="username cannot be empty")
        @Length(min = 3, max = 20, message = "Username must be 3-50 characters long")
        @Pattern(regexp = "[a-zA-Z0-9-_.]*", message = "username can only consist of numbers, letters, '_', '-', and '.'")
        private String newusername;

        public String getnewusername() {
            return this.newusername;
        }

        public void setnewusername(String newusername) {
            this.newusername = newusername;
        }
    }

}
