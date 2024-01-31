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

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

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

    @GetMapping("/project/{projectId}/sighting/new")
    public String submitSightingForm(@PathVariable int projectId, Model model) throws SQLException {
        model.addAttribute("values", new SubmitSightingForm());
        ProjectService.Project project = projectService.getProjectById(projectId);
        setupSubmitSightingFormModel(project, model);
        return "submit_sighting";
    }

    @PostMapping("/project/{projectId}/sighting/new")
    public String submitSightingSubmit(@PathVariable int projectId, @ModelAttribute("values") SubmitSightingForm values, BindingResult bindingResult, Model model) throws SQLException {
        ProjectService.Project project = projectService.getProjectById(projectId);

        if (values.getImage() != null && values.getImage().getSize() > 0) {
            try {
                // Get metadata from the image
                MetadataService.Metadata metadata = metadataService.extractMetadata(values.getImage().getInputStream());

                // Fill the metadata into the form
                values.setLatitude(metadata.getLatitude());
                values.setLongitude(metadata.getLongitude());
                values.setTimestamp(metadata.getTimestamp());

                String name = imageUploadService.uploadImage(values.getImage().getInputStream(), metadata.getDetectedExtension());
                values.setImageUrl(UserContentController.PREFIX + name);

                values.setImageStage(2);
            } catch (UnsupportedImageTypeException e) {
                String message = "File type not supported. Supported: " + String.join(", ", e.getAllowedExtensions()) + ".";
                bindingResult.rejectValue("imageUrl", "error.values", message);
            } catch (Exception e) {
                // Put the error into the logs
                new Exception(
                        "Error occurred processing uploaded image (name=" + values.getImage().getName() + ", size=" + values.getImage().getSize() + ")"
                ).printStackTrace();
                bindingResult.rejectValue("imageUrl", "error.values", "your uploaded image could not be processed");
            }
        }

        validator.validate(values, bindingResult);

        if (bindingResult.hasErrors() || project.isCurrentlyClosed()) {
            setupSubmitSightingFormModel(project, model);
            return "submit_sighting";
        }

        return "redirect:/";
    }

    private void setupSubmitSightingFormModel(ProjectService.Project project, Model model) throws SQLException {
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("project", project);

        List<OrganismService.Organism> organisms = organismService.getOrganismsForProject(project.getId());
        model.addAttribute("organismOptions", organisms);
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
