package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.SettingsService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UpdatePasswordController {

    @Autowired
    private SettingsService settings;

    @Autowired
    private UserService userService;

    @GetMapping("/settings/updatepassword")
    public String updateDisplayName(Model model) {
        model.addAttribute("passwordForm", new UpdatePasswordForm());
        return "update_password";
    }

    @PostMapping("/settings/updatepassword")
    public String updateDisplayName(@Valid @ModelAttribute("passwordForm") UpdatePasswordForm passwordForm, BindingResult bindingResult, Model model) {
        model.addAttribute("passwordForm",passwordForm);
        User user = userService.getCurrentUser();
        int userID = user.getId();
        System.out.println(userID);

        try{
            settings.updatePassword(userID, passwordForm.getoldpassword(), passwordForm.getnewpassword(), passwordForm.getconfnewpassword());
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
        return "redirect:/settings";
    }


    public static class UpdatePasswordForm {
        @NotEmpty
        private String oldpassword;

        @NotEmpty
        private String newpassword;

        @NotEmpty
        private String confnewpassword;

        public String getoldpassword() {
            return this.oldpassword;
        }

        public void setoldpassword(String oldpassword) {
            this.oldpassword = oldpassword;
        }

        public String getnewpassword() {
            return this.newpassword;
        }

        public void setnewpassword(String newpassword) {
            this.newpassword = newpassword;
        }

        public String getconfnewpassword() {
            return this.confnewpassword;
        }

        public void setconfnewpassword(String confnewpassword) {
            this.confnewpassword = confnewpassword;
        }

    }
}
