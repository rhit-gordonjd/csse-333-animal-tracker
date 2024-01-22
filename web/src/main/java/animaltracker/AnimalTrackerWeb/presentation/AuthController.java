package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ATUserDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ATUserDetailsService userService;

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
            userService.registerUser(values.getUsername(), values.getPassword());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }


    public static class RegisterForm {
        @NotNull
        @NotEmpty
        private String username;

        @NotNull
        @NotEmpty
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
