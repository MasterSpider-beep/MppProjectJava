package mpp.repository;

import mpp.domain.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    public Optional<Ticket> getOne(int id);
    public List<Ticket> getAll();
    public boolean addOne(Ticket ticket);
}
