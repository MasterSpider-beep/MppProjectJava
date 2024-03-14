import mpp.domain.DTOs.FlightFilter;
import mpp.domain.Flight;
import mpp.repository.DBFlightsRepository;
import mpp.repository.DBUsersRepository;
import mpp.repository.FlightsRepository;
import mpp.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class RepositoriesTests {
    @Test
    @DisplayName("UserRepositoryTest")
    public void testDBUserRepo(){
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        UsersRepository repo = new DBUsersRepository(props);
        assert (repo.existsUser("admin", "admin"));
    }

    @Test
    @DisplayName("FlightsRepositoryTest")
    public void TestDBFlightRepository(){
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        FlightsRepository repo = new DBFlightsRepository(props);
        List<Flight> flights = repo.getFiltered(new FlightFilter("test"));
        assert (flights.size()==2);
        flights = repo.getFiltered(new FlightFilter("test", LocalDate.of(2024,1,1)));
        assert (flights.size()==1);
        Flight flight = flights.get(0);
        flight.setAvailableSeats(20);
        repo.updateFlight(flight);
        flights = repo.getFiltered(new FlightFilter("test", LocalDate.of(2024,1,1)));
        assert (flights.get(0).getAvailableSeats()==20);
        flight.setAvailableSeats(10);
        repo.updateFlight(flight);
    }
}
