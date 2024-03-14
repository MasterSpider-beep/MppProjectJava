package mpp.repository;

import mpp.domain.DTOs.FlightFilter;
import mpp.domain.Flight;

import java.util.List;

public class DBFlightsRepository implements FlightsRepository{
    @Override
    public List<Flight> getAll() {
        return getFiltered(new FlightFilter());
    }

    @Override
    public List<Flight> getFiltered(FlightFilter filter) {
        return null;
    }

    @Override
    public boolean updateFlight(Flight flight) {
        return false;
    }
}
