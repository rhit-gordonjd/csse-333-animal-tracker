package org.example.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private Properties properties;

    public Config() throws IOException {
        properties = new Properties();
        try (InputStream propertiesFile = Files.newInputStream(Path.of("config.properties"))) {
            properties.load(propertiesFile);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new RuntimeException("Required config key \"" + key + "\" is missing");
        }
        return value;
    }

    /**
     * Fills a template string with corresponding config values
     *
     * @param template A template string containing ${keys}
     * @return The template string with all occurrences of ${key} replaced with getRequiredProperty("key")
     */
    public String fillTemplate(String template) {
        return Pattern.compile("\\$\\{(.*?)\\}")
                .matcher(template)
                .replaceAll((match) -> Matcher.quoteReplacement(getRequiredProperty(match.group(1))));
    }
}
