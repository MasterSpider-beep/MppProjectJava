package domain.DTOs;

import java.time.LocalDate;
import java.util.Optional;

public class FlightFilter {
    private String destination;
    private LocalDate departureDate;

    public FlightFilter(String destination, LocalDate departureDate) {
        this.destination = destination;
        this.departureDate = departureDate;
    }

    public FlightFilter() {
        this.destination = null;
        this.departureDate = null;
    }

    public boolean isEmpty(){
        return destination==null&&departureDate==null;
    }

    public Optional<String> getDestination() {
        return destination==null?Optional.empty():Optional.of(destination);
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Optional<LocalDate> getDepartureDate() {
        return departureDate==null?Optional.empty():Optional.of(departureDate);
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public FlightFilter(LocalDate departureDate) {
        this.departureDate = departureDate;
        this.destination = null;
    }

    public FlightFilter(String destination) {
        this.destination = destination;
        this.departureDate = null;
    }
}
