package mpp.repository;

import mpp.domain.DTOs.FlightFilter;
import mpp.domain.Flight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBFlightsRepository implements FlightsRepository{

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public DBFlightsRepository(Properties properties) {
        logger.info("Initializing DBFlightsRepository with poroperties: {}", properties);
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Flight> getAll() {
        return getFiltered(new FlightFilter());
    }

    @Override
    public List<Flight> getFiltered(FlightFilter filter) {
        List<Flight> flights = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        logger.traceEntry();
        String sqlString = getFilterSqlString(filter);
        try(PreparedStatement statement = connection.prepareStatement(sqlString)) {
            int i=1;
            if(filter.getDestination().isPresent()){
                statement.setString(i,filter.getDestination().get());
                i++;
            }
            if(filter.getDepartureDate().isPresent()){
                statement.setString(i, filter.getDepartureDate().get().toString());
                i++;
            }
            try(ResultSet result = statement.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    String destination = result.getString("destination");
                    LocalDate departureDate = LocalDate.parse(result.getString("departureDate"));
                    LocalTime departureTime = LocalTime.parse(result.getString("departureTime"));
                    int availableSeats =result.getInt("availableSeats");
                    String airport = result.getString("airport");

                    Flight flight1 = new Flight(destination,departureDate,departureTime,availableSeats,airport);
                    flight1.setId(id);
                    flights.add(flight1);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(flights);
        return flights;
    }

    private static String getFilterSqlString(FlightFilter filter) {
        String sqlString = "SELECT * FROM Flights";
        if(!filter.isEmpty()){
            sqlString+=" WHERE";
            if(filter.getDestination().isPresent()){
                sqlString+=" destination = ?";
            }
            if(filter.getDepartureDate().isPresent() && filter.getDestination().isPresent()){
                sqlString+=" AND departureDate = ?";
            } else if (filter.getDepartureDate().isPresent()) {
                sqlString+=" departureDate = ?";
            }
        }
        return sqlString;
    }

    @Override
    public boolean updateFlight(Flight flight){
        logger.traceEntry("Update task {} elem",flight);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement
                ("UPDATE Flights SET destination = ? , departureDate=?, departureTime=? , availableSeats = ?, airport = ? WHERE id = ?")){
            statement.setString(1,flight.getDestination());
            statement.setString(2,flight.getDepartureDate().toString());
            statement.setString(3,flight.getDepartureTime().toString());
            statement.setInt(4,flight.getAvailableSeats());
            statement.setString(5,flight.getAirport());
            statement.setInt(6,flight.getId());
            int result = statement.executeUpdate();
            logger.trace("Updated {} entries",result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        return true;
    }
}
