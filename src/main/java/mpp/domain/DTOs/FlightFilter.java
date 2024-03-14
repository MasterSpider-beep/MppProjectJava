package mpp.domain.DTOs;

import java.time.LocalDate;

public class FlightFilter {
    private String destination = null;
    private LocalDate departureDate = null;

    public FlightFilter(String destination, LocalDate departureDate) {
        this.destination = destination;
        this.departureDate = departureDate;
    }

    public FlightFilter() {
        this.destination = null;
        this.departureDate = null;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public FlightFilter(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public FlightFilter(String destination) {
        this.destination = destination;
    }
}
