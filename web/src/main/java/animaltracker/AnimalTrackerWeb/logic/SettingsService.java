package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsService {
    @Autowired
    private UserSettingsService userSettings;

    public boolean updateDisplayName(int UserID, String newDisplayName) {
        return userSettings.updateDisplayName(UserID, newDisplayName);
    }
}
