package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimalTrackerUserDetailsService implements UserDetailsService {
    @Autowired
    UserDataService data;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserDataService.User userData = data.getUserByUsername(username);

        if (username.equals("user")) {
            return new User("user", "{noop}password", List.of());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
