package mpp.repository;

import mpp.domain.Ticket;
import java.util.List;

public interface TicketRepository {
    public Ticket getOne(int id);
    public List<Ticket> getAll();
    public boolean addOne(Ticket ticket);
}
