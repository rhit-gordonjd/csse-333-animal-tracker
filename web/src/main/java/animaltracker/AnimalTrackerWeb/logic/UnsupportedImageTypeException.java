package animaltracker.AnimalTrackerWeb.logic;

import java.util.List;

public class UnsupportedImageTypeException extends Exception {

    private final String extension;
    private final List<String> allowedExtensions;

    public UnsupportedImageTypeException(String extension, List<String> allowedExtensions) {
        super("image type is not allowed");

        this.extension = extension;
        this.allowedExtensions = allowedExtensions;
    }

    public String getExtension() {
        return extension;
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }
}
