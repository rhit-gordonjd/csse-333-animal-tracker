package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.UserDataService;
import animaltracker.AnimalTrackerWeb.data.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class SettingsService {
    @Autowired
    private UserSettingsService userSettings;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserDataService dataService;

    public boolean updateDisplayName(int userID, String newDisplayName) {
        return userSettings.updateDisplayName(userID, newDisplayName);
    }

    public boolean updateUsername(int userID, String newUsername) {
        return userSettings.updateUsername(userID, newUsername);
    }

    public boolean checkPassword(int userID, String password) {
        try {
            String oldpass = dataService.getUserById(userID).getEncodedPassword();
            if (passwordEncoder.matches(password, oldpass)) {
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int UserID, String oldpassword, String newpassword, String confnewpassword) {
        boolean checkold = checkPassword(UserID, oldpassword);
        boolean checknew = newpassword.equals(confnewpassword);
        if(checkold && checknew) {
            return userSettings.updatePassword(UserID, passwordEncoder.encode(newpassword));
        }
        return false;
    }

    public boolean verifyOldPassword(int UserID, String oldpassword) {
        return checkPassword(UserID, oldpassword);
    }

    public boolean verifyNewPassword(int UserID, String newpassword, String confnewpassword) {
        return newpassword.equals(confnewpassword);
    }
}
