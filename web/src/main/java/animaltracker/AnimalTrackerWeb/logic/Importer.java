package animaltracker.AnimalTrackerWeb.logic;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Component
public class Importer {
    @Autowired
    private DataSource dataSource;

    private static final DateTimeFormatter timestampFormat = DateTimeFormatter
            .ofPattern("M/d/yyyy h:mm a")
            .withZone(ZoneId.of("America/Indiana/Indianapolis"));

    public Report importProjectsFile(InputStream data) throws IOException, SQLException {
        return importCSVFile(data, this::importProject);
    }

    public Report importSightingsFile(InputStream data) throws IOException, SQLException {
        return importCSVFile(data, this::importSighting);
    }

    private Report importCSVFile(InputStream data, ImportMethod importer) throws IOException, SQLException {
        StringBuilder errors = new StringBuilder();

        int total = 0;
        int failures = 0;

        try (Connection connection = dataSource.getConnection();
             InputStreamReader reader = new InputStreamReader(data)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                total++;

                try {
                    importer.doImport(connection, record);
                } catch (Exception e) {
                    failures++;

                    errors.append("Error importing record #");
                    errors.append(record.getRecordNumber());
                    errors.append(": ");
                    errors.append(e);
                    errors.append("\n");

                    new Exception("Error importing " + record, e).printStackTrace();
                }
            }
        }

        String errorsString = null;
        if (failures > 0) {
            errorsString = errors.toString();
        }

        return new Report(total - failures, failures, errorsString);
    }

    private void importProject(Connection connection, CSVRecord record) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(
                "{? = call ImportProject(@Name = ?, @Description = ?, @StartedBy = ?, @Animals = ?)}")) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, record.get("Name"));
            stmt.setString(3, record.get("Description"));
            stmt.setString(4, record.get("Started By"));
            stmt.setString(5, record.get("Animals"));

            stmt.executeUpdate();
            int status = stmt.getInt(1);

            if (status != 0) {
                throw new Exception("Stored Procedure ImportProject returned " + status);
            }
        }
    }

    private void importSighting(Connection connection, CSVRecord record) throws Exception {
        Timestamp timestamp = parseTimestamp(record.get("Date"), record.get("Time"));

        try (CallableStatement stmt = connection.prepareCall(
                "{? = call ImportSighting(@Timestamp = ?, @ProjectName = ?, @Person = ?, @CommonName = ?, @ScientificName = ?, @Latitude = ?, @Longitude = ?, @ImageURL = ?)}")) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setTimestamp(2, timestamp);
            stmt.setString(3, record.get("Project Name"));
            stmt.setString(4, record.get("Person"));
            stmt.setString(5, record.get("Common Name"));
            stmt.setString(6, record.get("Scientific Name"));
            stmt.setString(7, record.get("Latitude"));
            stmt.setString(8, record.get("Longitude"));
            stmt.setString(9, record.get("Image URL"));

            stmt.executeUpdate();
            int status = stmt.getInt(1);

            if (status != 0) {
                throw new Exception("Stored Procedure ImportSighting returned " + status);
            }
        }
    }

    public static Timestamp parseTimestamp(String date, String time) {
        Instant instant = timestampFormat.parse(date + " " + time, Instant::from);
        return Timestamp.from(instant);
    }

    public static class Report {
        private final int countOk;
        private final int countBad;
        private final String errors;

        public Report(int countOk, int countBad, String errors) {
            this.countOk = countOk;
            this.countBad = countBad;
            this.errors = errors;
        }

        public int getCountOk() {
            return countOk;
        }

        public int getCountBad() {
            return countBad;
        }

        public String getErrors() {
            return errors;
        }
    }

    private interface ImportMethod {
        void doImport(Connection connection, CSVRecord record) throws Exception;
    }
}
