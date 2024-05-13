package domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "Tickets")
public class Ticket {
    private Integer id;
    private String clientName;
    private String tourists;
    private int flightId;
    private String address;
    private int noSeats;
    public Ticket(){this.tourists = "";}
    public Ticket (String clientName, int flightId, String address, int noSeats){
        this.clientName = clientName;
        this.flightId = flightId;
        this.address = address;
        this.noSeats = noSeats;
        this.tourists = "";
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer getId() {return id;}
    public void setId(int id){this.id=id;}

    @NotNull
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTourists() {
        return tourists;
    }

    @Column(name = "flight_id")
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public String toString() {
        return "TicketHibernate{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", tourists='" + tourists + '\'' +
                ", flightId=" + flightId +
                ", address='" + address + '\'' +
                ", noSeats=" + noSeats +
                '}';
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTourists(String tourists) {
        this.tourists = tourists;
    }

    public int getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(int noSeats) {
        this.noSeats = noSeats;
    }

    public void addTourists(List<String> touristsList) {
        touristsList.forEach(tourist->{tourists+=tourist;});
    }
}
