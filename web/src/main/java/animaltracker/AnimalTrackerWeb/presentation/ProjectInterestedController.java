package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ProjectService;
import animaltracker.AnimalTrackerWeb.logic.User;
import animaltracker.AnimalTrackerWeb.logic.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;

@Controller
public class ProjectInterestedController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @PostMapping("/projects/{projectId}/interested")
    public RedirectView setInterested(
            @PathVariable int projectId,
            @Valid SetInterestedForm values
    ) throws SQLException {
        User currentUser = userService.getCurrentUser();
        projectService.setInterested(currentUser, values.getProjectId(), values.getInterested());

        RedirectView redirectView = new RedirectView(values.returnTo);
        redirectView.setExpandUriTemplateVariables(false); // Prevent injection attacks

        return redirectView;
    }

    public static class SetInterestedForm {
        private int projectId;
        private boolean interested;
        @NotNull
        private String returnTo;

        public int getProjectId() {
            return projectId;
        }

        public boolean getInterested() {
            return interested;
        }

        public String getReturnTo() {
            return returnTo;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public void setInterested(boolean interested) {
            this.interested = interested;
        }

        public void setReturnTo(String returnTo) {
            this.returnTo = returnTo;
        }
    }
}
