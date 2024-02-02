package animaltracker.AnimalTrackerWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AnimalTrackerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalTrackerWebApplication.class, args);
    }

}
