package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class ImageUploadService {
    private static final Map<String, MediaType> mimeTypes = Map.of(
            "png", MediaType.IMAGE_PNG,
            "jpg", MediaType.IMAGE_JPEG
    );
    private static final Set<String> allowedExtensions = mimeTypes.keySet();

    @Autowired
    private StorageService storageService;

    public String uploadImage(InputStream inputStream, String extension) throws UnsupportedImageTypeException, IOException {
        if (!allowedExtensions.contains(extension)) {
            throw new UnsupportedImageTypeException(extension, allowedExtensions.stream().toList());
        }

        return storageService.save(inputStream, extension);
    }

    public InputStream load(String name) throws IOException {
        return storageService.load(name);
    }

    public MediaType getMediaType(String name) {
        String[] parts = name.split("\\.");
        String extension = parts[parts.length - 1].toLowerCase(Locale.ROOT);
        return mimeTypes.get(extension);
    }
}
