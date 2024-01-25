package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private static final String alreadyAuthedPage = "redirect:/";

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        if (userAlreadyAuthed()) {
            return alreadyAuthedPage;
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        if (userAlreadyAuthed()) {
            return alreadyAuthedPage;
        }

        model.addAttribute("values", new RegisterForm());
        model.addAttribute("showGenericError", false);
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("values") @Valid RegisterForm values, BindingResult bindingResult, Model model) {
        if (userAlreadyAuthed()) {
            return alreadyAuthedPage;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showGenericError", false);
            return "register";
        }

        try {
            userService.registerUser(values.getUsername(), values.getDisplayName(), values.getPassword());
        } catch (Exception e) {
            new Exception("An error occurred when registering user with username " + values.username, e).printStackTrace();

            model.addAttribute("showGenericError", true);
            return "register";
        }

        return "redirect:/login?registered";
    }

    /**
     * Returns true if the user is already logged in, so we shouldn't show them a login or registration page
     */
    private boolean userAlreadyAuthed() {
        return userService.getCurrentUser() != null;
    }


    public static class RegisterForm {
        @NotEmpty
        @Length(min = 3, max = 20)
        @Pattern(regexp = "[a-zA-Z0-9-_.]*", message = "username can only consist of numbers, letters, '_', '-', and '.'")
        private String username;

        @NotEmpty
        @Length(max = 50)
        private String displayName;

        @NotEmpty
        private String password;

        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
