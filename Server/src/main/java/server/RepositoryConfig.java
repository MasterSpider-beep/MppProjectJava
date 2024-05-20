package server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import repository.DBFlightsRepository;
import repository.FlightsRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepositoryConfig {
    @Bean
    public FlightsRepository flightsRepository(){
        Properties props=new Properties();
        try {
            props.load(RepositoryConfig.class.getResourceAsStream("/bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        return new DBFlightsRepository(props);
    }
}
