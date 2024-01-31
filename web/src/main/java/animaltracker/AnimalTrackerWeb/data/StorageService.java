package animaltracker.AnimalTrackerWeb.data;

import animaltracker.AnimalTrackerWeb.logic.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Controller
public class StorageService {
    private static final String safeFileNamePattern = "[0-9A-Za-z-][0-9A-Za-z-.]*";
    private final Path root;

    private final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);

    public StorageService(StorageProperties properties) throws IOException {
        root = Path.of(properties.getLocation());
        if (!Files.isDirectory(root)) {
            logger.info("Creating uploads directory at " + root);
            Files.createDirectories(root);
        }
    }

    private String generateName(String extension) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        return id + "." + extension;
    }

    private Path resolve(String name) {
        if (name.matches(safeFileNamePattern)) {
            return root.resolve(name);
        } else {
            return null;
        }
    }

    /**
     * Saves data into the file store
     *
     * @param data      The data to save
     * @param extension The extension of the saved file
     * @return The name that the data was saved to
     */
    public String save(InputStream data, String extension) throws IOException {
        String name = generateName(extension);
        Path path = resolve(name);
        logger.info("Saving uploaded file to " + path);
        if (path == null) {
            throw new IOException("Invalid file path");
        }
        Files.copy(data, path);
        return name;
    }

    /**
     * Opens the file specified by name
     *
     * @param name The name of the file to open
     * @return The opened file, or null if the file does not exist
     */
    public InputStream load(String name) throws IOException {
        Path path = resolve(name);
        if (path == null) {
            return null;
        }
        return Files.newInputStream(path);
    }
}
