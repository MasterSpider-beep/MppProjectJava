package domain.DTOs;

public class TicketDTO {
    private int flightId;
    private String clientName;
    private String tourists;    
    private String address;
    private int noSeats;

    @Override
    public String toString() {
        return "TicketDTO{" +
                "flightId=" + flightId +
                ", clientName='" + clientName + '\'' +
                ", tourists='" + tourists + '\'' +
                ", address='" + address + '\'' +
                ", noSeats=" + noSeats +
                '}';
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTourists() {
        return tourists;
    }

    public void setTourists(String tourists) {
        this.tourists = tourists;
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

    public TicketDTO(int flightId, String clientName, String tourists, String address, int noSeats) {
        this.flightId = flightId;
        this.clientName = clientName;
        this.tourists = tourists;
        this.address = address;
        this.noSeats = noSeats;
    }
}
