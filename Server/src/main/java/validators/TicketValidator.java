package validators;

import domain.Ticket;

public class TicketValidator{
    public boolean validate(Ticket ticket) {
        if (ticket.getNoSeats()<=0)
            return false;
        if(ticket.getTourists().isEmpty())
            return false;
        if (ticket.getAddress().isBlank())
            return false;
        if (ticket.getClientName().isBlank())
            return false;
        return true;
    }
}
