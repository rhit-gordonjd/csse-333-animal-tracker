package animaltracker.AnimalTrackerWeb.presentation.formatter;

import animaltracker.AnimalTrackerWeb.logic.OrganismService;
import org.springframework.format.Printer;

import java.util.Locale;

public class OrganismFormatter implements Printer<OrganismService.Organism> {
    @Override
    public String print(OrganismService.Organism object, Locale locale) {
        String commonName = object.getCommonName();
        String scientificName = object.getScientificName();
        if ((commonName == null || commonName.isEmpty()) && (scientificName == null || scientificName.isEmpty())) {
            return "Unknown Organism";
        } else if (commonName == null || commonName.isEmpty()) {
            return scientificName;
        } else if (scientificName == null || scientificName.isEmpty()) {
            return commonName;
        } else {
            return commonName + " (" + scientificName + ")";
        }
    }
}
