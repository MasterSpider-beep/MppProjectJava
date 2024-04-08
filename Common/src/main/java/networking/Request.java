package networking;

import domain.DTOs.FlightFilter;
import domain.DTOs.TicketDTO;
import domain.User;

public class Request {
    private RequestType type;
    private User user;
    private FlightFilter filter;
    private TicketDTO ticket;

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", user=" + user +
                ", filter=" + filter +
                ", ticket=" + ticket +
                '}';
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FlightFilter getFilter() {
        return filter;
    }

    public void setFilter(FlightFilter filter) {
        this.filter = filter;
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }

    public Request() {
    }
}
