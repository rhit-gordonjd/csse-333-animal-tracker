package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.data.SightingDataService;
import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;

@Controller
public class SightingsDeletionController {
    @Autowired
    private SightingDataService sightingsService;

    @PostMapping("/sightings/delete")
    public String deleteSighting(
            @Valid DeleteSightingForm values
    ) throws SQLException {
        sightingsService.deleteSighting(values.getSightingId());
        return "redirect:/my_sightings";
    }

    public static class DeleteSightingForm {
        private int sightingId;

        public int getSightingId() {
            return sightingId;
        }

        public void setSightingId(int sightingId) {
            this.sightingId = sightingId;
        }
    }
}
