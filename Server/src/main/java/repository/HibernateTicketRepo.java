package repository;

import domain.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateTicketRepo implements TicketRepository{

    @Override
    public Optional<Ticket> getOne(int id) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try(Session session = sessionFactory.openSession()){
            Ticket result = session.createQuery("from Ticket where id=:idP", Ticket.class)
                    .setParameter("idP", id).getSingleResultOrNull();
            if(result == null){
                return Optional.empty();
            }
            return Optional.of(result);
        }
    }

    @Override
    public List<Ticket> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("from Ticket ", Ticket.class).setFirstResult(0).list();
        }
    }

    @Override
    public boolean addOne(Ticket ticket) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        sessionFactory.inTransaction(session -> {
            session.persist(ticket);
        });
        return true;
    }
}
