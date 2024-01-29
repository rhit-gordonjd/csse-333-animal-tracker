package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.OrganismService;
import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@Controller
public class SightingsController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private OrganismService organismService;

    @GetMapping("/project/{projectId}/sighting/new")
    public String submitSightingForm(@PathVariable int projectId, Model model) throws SQLException {
        model.addAttribute("values", new SubmitSightingForm());
        ProjectService.Project project = projectService.getProjectById(projectId);
        setupSubmitSightingFormModel(project, model);
        return "submit_sighting";
    }

    @PostMapping("/project/{projectId}/sighting/new")
    public String submitSightingSubmit(@PathVariable int projectId, @ModelAttribute("values") @Valid SubmitSightingForm values, BindingResult bindingResult, Model model) throws SQLException {
        ProjectService.Project project = projectService.getProjectById(projectId);

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

        public Integer getOrganism() {
            return organism;
        }

        public void setOrganism(Integer organism) {
            this.organism = organism;
        }
    }
}
