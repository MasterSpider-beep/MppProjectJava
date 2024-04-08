package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Flight extends Entity{
    private String destination;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private int availableSeats;
    private String airport;

    public Flight() {
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

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public Flight(String destination, LocalDate departureDate, LocalTime departureTime, int availableSeats, String airport) {
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.airport = airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Flight flight = (Flight) o;
        return availableSeats == flight.availableSeats && Objects.equals(destination, flight.destination) && Objects.equals(departureDate, flight.departureDate) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(airport, flight.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), destination, departureDate, departureTime, availableSeats, airport);
    }
}
