package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
public class SightingsController {
    @Autowired
    private Validator validator;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private OrganismService organismService;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private UserService userService;
    @Autowired
    private SightingService sightingService;

    @GetMapping("/projects/{projectId}/sightings/new")
    public String submitSightingForm(@PathVariable int projectId, Model model) throws SQLException {
        model.addAttribute("values", new SubmitSightingForm());

        ProjectService.Project project = projectService.getProjectById(projectId);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("project", project);

        List<OrganismService.Organism> organisms = organismService.getOrganismsForProject(project.getId());
        model.addAttribute("organismOptions", organisms);

        return "submit_sighting";
    }

    @PostMapping("/projects/{projectId}/sightings/new")
    public String submitSightingSubmit(@PathVariable int projectId, @ModelAttribute("values") SubmitSightingForm values, BindingResult bindingResult, Model model) throws SQLException {
        ProjectService.Project project = projectService.getProjectById(projectId);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<OrganismService.Organism> organisms = organismService.getOrganismsForProject(project.getId());

        if (values.getImage() != null && values.getImage().getSize() > 0) {
            try {
                // Get metadata from the image
                MetadataService.Metadata metadata = metadataService.extractMetadata(values.getImage().getInputStream());

                // Fill the metadata into the form
                if (metadata.getLocation() != null) {
                    values.setLatitude(metadata.getLocation().getLatitude());
                    values.setLongitude(metadata.getLocation().getLongitude());
                }
                if (metadata.getTimestamp() != null) {
                    values.setTimestamp(metadata.getTimestamp());
                }

                // Save the image
                String imageUrl = saveImage(values.getImage(), metadata.getDetectedExtension());

                // Save the resulting url in values
                values.setImageUrl(imageUrl);

                // Move onto the next stage, if necessary
                values.setImageStage(2);
            } catch (UnsupportedImageTypeException e) {
                String message = "File type not supported. Supported: " + String.join(", ", e.getAllowedExtensions()) + ".";
                bindingResult.rejectValue("imageUrl", "error.values", message);
            } catch (Exception e) {
                // Put the error into the logs
                new Exception(
                        "Error occurred processing uploaded image (name=" + values.getImage().getName() + ", size=" + values.getImage().getSize() + ")", e
                ).printStackTrace();
                bindingResult.rejectValue("imageUrl", "error.values", "your uploaded image could not be processed");
            }
        }

        validator.validate(values, bindingResult);

        // Validate organism ID
        OrganismService.Organism organism = null;
        if (values.getOrganism() != null) {
            Optional<OrganismService.Organism> matchingOrganism = organisms.stream().filter(o -> o.getId() == values.getOrganism()).findAny();
            if (matchingOrganism.isPresent()) {
                organism = matchingOrganism.get();
            } else {
                bindingResult.rejectValue("organism", "error.values", "invalid organism selected");
            }
        }

        if (bindingResult.hasErrors() || project.isCurrentlyClosed()) {
            model.addAttribute("project", project);
            model.addAttribute("organismOptions", organisms);
            return "submit_sighting";
        }

        int sightingId = sightingService.createSighting(
                userService.getCurrentUser(),
                organism,
                values.timestamp,
                new Location(values.getLatitude(), values.getLongitude()),
                values.getImageUrl()
        );

        // TODO: Redirect to project page or sighting page (using sightingId)
        return "redirect:/";
    }

    @GetMapping("/my_sightings")
    public String getMySightings(Model model) throws SQLException {
        User currentUser = userService.getCurrentUser();

        model.addAttribute("sightings", sightingService.getUserSightings(currentUser));

        return "my_sightings";
    }

    private String saveImage(MultipartFile image, String extension) throws UnsupportedImageTypeException, IOException, SQLException {
        return imageUploadService.save(
                image.getInputStream(),
                extension,
                fileName -> UserContentController.PREFIX + fileName,
                userService.getCurrentUser()
        );
    }

    public static class SubmitSightingForm {
        @NotNull(message = "please select an organism")
        private Integer organism;
        private int imageStage = 1;
        private MultipartFile image;
        @NotEmpty(message = "please upload an image")
        private String imageUrl;
        @NotNull(message = "please specify a location")
        private Double latitude;
        @NotNull(message = "please specify a location")
        private Double longitude;
        @NotNull(message = "please specify a timestamp")
        private Instant timestamp;

        public Integer getOrganism() {
            return organism;
        }

        public int getImageStage() {
            return imageStage;
        }

        public MultipartFile getImage() {
            return image;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setOrganism(Integer organism) {
            this.organism = organism;
        }

        public void setImageStage(int imageStage) {
            this.imageStage = imageStage;
        }

        public void setImage(MultipartFile image) {
            this.image = image;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }
    }
}
