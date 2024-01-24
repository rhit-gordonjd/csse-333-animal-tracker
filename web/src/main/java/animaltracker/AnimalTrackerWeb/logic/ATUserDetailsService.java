package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class ATUserDetailsService implements UserDetailsService {
    @Autowired
    UserDataService data;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ATUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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

        return new ATUserDetails(userData);
    }

    public void registerUser(String username, String displayName, String password) throws Exception {
        data.createUser(username, displayName, passwordEncoder.encode(password));
    }

    public static class ATUserDetails implements UserDetails {
        public final int id;
        private final String username;
        private final String displayName;
        private final String encodedPassword;
        private final List<GrantedAuthority> authorities = List.of();

        ATUserDetails(UserDataService.UserDTO data) {
            this.id = data.getId();
            this.username = data.getUsername();
            this.displayName = data.getDisplayName();
            this.encodedPassword = data.getEncodedPassword();
        }

        public int getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String getPassword() {
            return encodedPassword;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
