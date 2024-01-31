package animaltracker.AnimalTrackerWeb.logic;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;

@Component
public class MetadataService {
    public Metadata extractMetadata(InputStream inputStream) throws IOException, ImageProcessingException {
        com.drew.metadata.Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

        GeoLocation location = null;
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (gpsDirectory != null) {
            location = gpsDirectory.getGeoLocation();
        }

        Date date = null;
        ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (exifDirectory != null) {
            date = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        }

        FileTypeDirectory fileTypeDirectory = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);
        String detectedExtension = fileTypeDirectory.getString(FileTypeDirectory.TAG_EXPECTED_FILE_NAME_EXTENSION);

        return new Metadata(location, date, detectedExtension);
    }

    public static class Metadata {
        private Location location = null;
        private Instant timestamp = null;
        private final String detectedExtension;

        private Metadata(GeoLocation location, Date timestamp, String detectedExtension) {
            this.detectedExtension = detectedExtension;
            if (location != null) {
                this.location = new Location(location.getLatitude(), location.getLongitude());
            }
            if (timestamp != null) {
                this.timestamp = timestamp.toInstant();
            }
        }

        public Location getLocation() {
            return location;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public String getDetectedExtension() {
            return detectedExtension;
        }
    }
}
