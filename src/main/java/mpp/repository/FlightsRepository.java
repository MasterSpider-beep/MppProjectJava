package mpp.repository;

import mpp.domain.DTOs.FlightFilter;
import mpp.domain.Flight;

import java.util.*;

public interface FlightsRepository {
    public List<Flight> getAll();
    public List<Flight> getFiltered(FlightFilter filter);
    public boolean updateFlight(Flight flight);
}
