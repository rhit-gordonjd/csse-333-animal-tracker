package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserDetailsControllerAdvice {
    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    public User populateCurrentUser() {
        return userService.getCurrentUser();
    }
}
