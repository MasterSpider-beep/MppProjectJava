package mpp.repository;

import mpp.domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBTicketRepository implements TicketRepository{

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public DBTicketRepository(Properties properties) {
        logger.info("Initializing DBTicketsRepository with properties: {}", properties);
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Optional<Ticket> getOne(int id) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Tickets WHERE id=?")) {
            statement.setInt(1,id);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    Ticket ticket = getTicket(id, result);
                    logger.traceExit(ticket);
                    return Optional.of(ticket);
                }else {
                    logger.traceExit("None found");
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private static Ticket getTicket(int id, ResultSet result) throws SQLException {
        String clientName = result.getString("clientName");
        String touristsTemp = result.getString("tourists");
        String address = result.getString("address");
        int noSeats = result.getInt("noSeats");
        List<String> tourists = Arrays.stream(touristsTemp.split(" ")).toList();
        Ticket ticket = new Ticket(clientName,address,noSeats);
        ticket.addTourists(tourists);
        ticket.setId(id);
        return ticket;
    }

    @Override
    public List<Ticket> getAll() {
        logger.traceEntry();
        List<Ticket>  tickets = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Tickets")) {
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Ticket ticket = getTicket(result.getInt("id"),result);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public boolean addOne(Ticket ticket) {
        logger.traceEntry("saving task {} elem", ticket);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO Tickets(clientName, tourists, address, noSeats) VALUES (?,?,?,?)")){
            statement.setString(1,ticket.getClientName());
            String tourists;
            List<String> touristsTemp = ticket.getTourists();
            StringBuilder builder = new StringBuilder();
            touristsTemp.forEach(builder::append);
            tourists = builder.toString();
            statement.setString(2,tourists);
            statement.setString(3,ticket.getAddress());
            statement.setInt(4,ticket.getNoSeats());
            int result = statement.executeUpdate();
            logger.trace("Saved {} entities",result);
            return result > 0;

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
