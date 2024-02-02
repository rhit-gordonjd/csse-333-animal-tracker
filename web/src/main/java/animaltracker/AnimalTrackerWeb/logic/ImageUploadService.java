package animaltracker.AnimalTrackerWeb.logic;

import animaltracker.AnimalTrackerWeb.data.StorageService;
import animaltracker.AnimalTrackerWeb.data.UploadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class ImageUploadService {
    private static final Map<String, MediaType> mimeTypes = Map.of(
            "png", MediaType.IMAGE_PNG,
            "jpg", MediaType.IMAGE_JPEG
    );
    private static final Set<String> allowedExtensions = mimeTypes.keySet();

    @Autowired
    private StorageService storageService;
    @Autowired
    private UploadDataService uploadDataService;

    public String save(InputStream inputStream, String extension, Function<String, String> urlMapper, User uploader) throws UnsupportedImageTypeException, IOException, SQLException {
        if (!allowedExtensions.contains(extension)) {
            throw new UnsupportedImageTypeException(extension, allowedExtensions.stream().toList());
        }

        String fileName = storageService.save(inputStream, extension);
        String url = urlMapper.apply(fileName);

        uploadDataService.createImage(url, uploader.getId());

        return url;
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
