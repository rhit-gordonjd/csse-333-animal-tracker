package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.Importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class ImportController {
    @Autowired
    private Importer importer;

    @GetMapping("/import")
    public String importForm(Model model) {
        return "import";
    }

    @PostMapping("/import")
    public String importSubmit(Model model, @RequestParam("projects") MultipartFile projectsFile, @RequestParam("sightings") MultipartFile sightingsFile) throws IOException, SQLException {
        Importer.Report projectsReport = importer.importProjectsFile(projectsFile.getInputStream());
        Importer.Report sightingsReport = importer.importSightingsFile(sightingsFile.getInputStream());

        model.addAttribute("projectsReport", projectsReport);
        model.addAttribute("sightingsReport", sightingsReport);
        return "import_report";
    }
}
