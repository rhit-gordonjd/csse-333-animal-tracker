package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.sql.SQLException;

@Component
@RequestScope
public class UserService {
    @Autowired
    private UserDataService data;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User currentUser;

    private Integer getCurrentUserID() {
        // TODO: Is there a better way to do this?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // We are not logged in
            return null;
        } else {
            // the current user id was previously stored as the authentication's name by ATUserDetailsServiceAdapter
            return Integer.parseInt(authentication.getName());
        }
    }

    private User fetchUser(int id) {
        UserDataService.UserDTO userData;
        try {
            userData = data.getUserById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (userData == null) {
            return null;
        }

        return new User(userData.getId(), userData.getUsername(), userData.getDisplayName());
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            Integer currentUserID = getCurrentUserID();
            if (currentUserID != null) {
                currentUser = fetchUser(currentUserID);
            }
        }

        return currentUser;
    }

    public void registerUser(String username, String displayName, String password) throws Exception {
        data.createUser(username, displayName, passwordEncoder.encode(password));
    }
}
