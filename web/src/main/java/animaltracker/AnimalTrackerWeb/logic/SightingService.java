package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.SightingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.Instant;

@Component
public class SightingService {
    @Autowired
    private SightingDataService sightingDataService;

    public int createSighting(User user, OrganismService.Organism organism, Instant timestamp, Location location, String imageURL) throws SQLException {
        return sightingDataService.createSighting(user.getId(), organism.getId(), timestamp, location.getLatitude(), location.getLongitude(), imageURL);
    }
}
