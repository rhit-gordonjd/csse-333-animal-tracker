package org.example.presentation;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.example.data.Config;
import org.example.data.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.function.Consumer;

public class ImportMain {
    private final Database database;

    public static void main(String[] args) throws IOException, SQLException {
        Config config = new Config();
        try (Database database = new Database(config)) {
            new ImportMain(database);
        }
    }

    public ImportMain(Database database) throws IOException {
        this.database = database;

        importCSVFile(Path.of("projects.csv"), this::importProject);
        importCSVFile(Path.of("sightings.csv"), this::importSighting);
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
        try (CallableStatement stmt = database.getConnection().prepareCall("{? = call ImportProject(@Name = ?, @Description = ?, @StartedBy = ?, @Animals = ?)}")) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, record.get("Name"));
            stmt.setString(3, record.get("Description"));
            stmt.setString(4, record.get("Started By"));
            stmt.setString(5, record.get("Animals"));

            stmt.executeUpdate();
            int status = stmt.getInt(1);

            if(status != 0){
                throw new SQLException("Stored Procedure ImportProject exited with code " + status);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void importSighting(CSVRecord record) {
        System.out.println("Importing sighting " + record);
    }
}