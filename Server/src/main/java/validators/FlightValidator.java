package validators;

import domain.Flight;

public class FlightValidator {
    public boolean validate(Flight flight){
        if(flight.getAvailableSeats()<0)
            return false;
        if(flight.getAirport().isBlank() || flight.getDestination().isBlank())
            return false;
        return true;
    }
}
