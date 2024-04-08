package repository;


import domain.DTOs.FlightFilter;
import domain.Flight;
import java.util.List;
import java.util.Optional;

public interface FlightsRepository {
    public List<Flight> getAll();
    public List<Flight> getFiltered(FlightFilter filter);
    public boolean updateFlight(Flight flight);
    public Optional<Flight> getOne(int id);
}
