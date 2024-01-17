package org.example.logic;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.example.data.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class Importer {
    private static final DateTimeFormatter timestampFormat = DateTimeFormatter
            .ofPattern("M/d/yyyy h:mm a")
            .withZone(ZoneId.of("America/Indiana/Indianapolis"));

    private final Database database;

    public Importer(Database database) {
        this.database = database;
    }

    public void importProjectsFile(Path path) throws IOException {
        importCSVFile(path, this::importProject);
    }

    public void importSightingsFile(Path path) throws IOException {
        importCSVFile(path, this::importSighting);
    }

    private void importCSVFile(Path path, Consumer<CSVRecord> importer) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                try {
                    importer.accept(record);
                } catch (Exception e) {
                    System.err.println("Error importing " + record);
                    e.printStackTrace();
                }
            }
        }
    }

    private void importProject(CSVRecord record) {
        System.out.println("Importing project " + record);
        try (CallableStatement stmt = database.getConnection().prepareCall(
                "{? = call ImportProject(@Name = ?, @Description = ?, @StartedBy = ?, @Animals = ?)}")) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, record.get("Name"));
            stmt.setString(3, record.get("Description"));
            stmt.setString(4, record.get("Started By"));
            stmt.setString(5, record.get("Animals"));

            stmt.executeUpdate();
            int status = stmt.getInt(1);

            if (status != 0) {
                throw new SQLException("Stored Procedure ImportProject returned " + status);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void importSighting(CSVRecord record) {
        System.out.println("Importing sighting " + record);

        Timestamp timestamp = parseTimestamp(record.get("Date"), record.get("Time"));

        try (CallableStatement stmt = database.getConnection().prepareCall(
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
                throw new SQLException("Stored Procedure ImportSighting returned " + status);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Timestamp parseTimestamp(String date, String time) {
        Instant instant = timestampFormat.parse(date + " " + time, Instant::from);
        return Timestamp.from(instant);
    }
}
