package animaltracker.AnimalTrackerWeb.presentation;

import animaltracker.AnimalTrackerWeb.presentation.formatter.OrganismFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addPrinter(new OrganismFormatter());
    }
}