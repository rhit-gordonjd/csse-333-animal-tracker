package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.logic.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class UserContentController {
    public static final String PREFIX = "/usercontent/";
    @Autowired
    ImageUploadService imageUploadService;

    @GetMapping(PREFIX + "{name}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> userImage(@PathVariable String name) throws IOException {
        InputStream inputStream = imageUploadService.load(name);

        if (inputStream == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(imageUploadService.getMediaType(name))
                .body(new InputStreamResource(inputStream));
    }
}
