package org.example.presentation;

import org.example.data.Config;
import org.example.data.Database;
import org.example.logic.Importer;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public class ImportMain {
    public static void main(String[] args) throws IOException, SQLException {
        Config config = new Config();
        try (Database database = new Database(config)) {
            Importer importer = new Importer(database);
            importer.importProjectsFile(Path.of("projects.csv"));
            importer.importSightingsFile(Path.of("sightings.csv"));
        }
    }
}