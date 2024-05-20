package RESTClient;

import domain.Flight;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class StartRESTClient {
    private final static NewFlightsClient flightClient = new NewFlightsClient();

    public static void main (String[] args){
        LocalDate date = LocalDate.of(2024, 1, 1);
        Flight flight = new Flight("destination",date,LocalTime.of(0,0),100,"Airport");
        try{
            System.out.println("Saving flight");
            int id = flightClient.create(flight);
            System.out.println("Printing all flights");
            Flight[] flights = flightClient.getAll();
            for (Flight flight1 : flights) {
                System.out.println(flight1);
            }
            flightClient.delete(id);

        }catch (RestClientException ex){
            throw new RuntimeException(ex);
        }
    }
}
