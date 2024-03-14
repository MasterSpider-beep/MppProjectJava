package mpp.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Ticket extends Entity {
    private String clientName;
    private final List<String> tourists = new ArrayList<>();

    private String address;
    private int noSeats;

    public List<String> getTourists() {
        return tourists;
    }

    public void addTourist(String tourist) {
        tourists.add(tourist);
    }

    public void addTourists(Collection<String> touristsToAdd) {
        tourists.addAll(touristsToAdd);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(int noSeats) {
        this.noSeats = noSeats;
    }

    public Ticket() {
    }

    public Ticket(String clientName, String address, int noSeats) {
        this.clientName = clientName;
        this.address = address;
        this.noSeats = noSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ticket ticket = (Ticket) o;
        return noSeats == ticket.noSeats && Objects.equals(clientName, ticket.clientName) && Objects.equals(tourists, ticket.tourists) && Objects.equals(address, ticket.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientName, tourists, address, noSeats);
    }
}
