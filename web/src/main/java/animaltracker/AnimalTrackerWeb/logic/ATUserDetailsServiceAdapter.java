package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Adapter class to be used internally by Spring Security.
 * Do not use directly. Use UserService instead.
 */
@Component
public class ATUserDetailsServiceAdapter implements UserDetailsService {
    @Autowired
    UserDataService data;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDataService.UserDTO userData;
        try {
            userData = data.getUserByUsername(username);
        } catch (SQLException e) {
            // TODO: How to handle this?
            throw new RuntimeException(e);
        }

        if (userData == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        // NOTE: We store the id in the username field instead of the actual username
        // as usernames may change over time and are not a stable identifier
        return new User(Integer.toString(userData.getId()), userData.getEncodedPassword(), List.of());
    }
}
