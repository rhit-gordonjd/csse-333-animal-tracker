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
public class UpdateDisplayNameController {

    @Autowired
    private SettingsService settings;

    @Autowired
    private UserService userService;

    @GetMapping("/settings/updatedisplayname")
    public String updateDisplayName(Model model) {
        model.addAttribute("form", new UpdateDisplayNameForm());
        return "update_display_name";
    }

    @PostMapping("/settings/updatedisplayname")
    public String updateDisplayName(@Valid @ModelAttribute("form") UpdateDisplayNameController.UpdateDisplayNameForm form, BindingResult bindingResult, Model model) {
        model.addAttribute("form",form);
        User user = userService.getCurrentUser();
        int userID = user.getId();

        try{
            settings.updateDisplayName(userID, form.getnewdisplayname());
        }catch(Exception e){
            System.out.println("Something went wrong");
        }
        return "redirect:/settings";
    }


    public static class UpdateDisplayNameForm {
        @NotEmpty
        @Length(max = 50)
        private String newdisplayname;

        public String getnewdisplayname() {
            return this.newdisplayname;
        }

        public void setnewdisplayname(String newdisplayname) {
            this.newdisplayname = newdisplayname;
        }
    }

}
