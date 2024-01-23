package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ATUserDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserDetailsControllerAdvice {
    @ModelAttribute("currentUser")
    public ATUserDetailsService.ATUserDetails populateCurrentUser(@AuthenticationPrincipal ATUserDetailsService.ATUserDetails details) {
        System.out.println("Populating currentUser = " + details);
        return details;
    }
}
