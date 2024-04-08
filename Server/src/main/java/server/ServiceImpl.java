package server;

import domain.DTOs.FlightFilter;
import domain.Flight;
import domain.Ticket;
import domain.User;
import exceptions.AppException;
import exceptions.LogInException;
import repository.FlightsRepository;
import repository.TicketRepository;
import repository.UsersRepository;
import service.IService;
import utils.IObserver;
import validators.FlightValidator;
import validators.TicketValidator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements IService {
    private final UsersRepository usersRepository;
    private final FlightsRepository flightsRepository;
    private final TicketRepository ticketRepository;
    private final TicketValidator ticketValidator = new TicketValidator();
    private final FlightValidator flightValidator = new FlightValidator();

    private Map<Integer, IObserver> loggedClients;

    public ServiceImpl(UsersRepository usersRepository, FlightsRepository flightsRepository, TicketRepository ticketRepository) {
        this.usersRepository = usersRepository;
        this.flightsRepository = flightsRepository;
        this.ticketRepository = ticketRepository;
        loggedClients = new ConcurrentHashMap<>();

    }

    public void logOut(User user, IObserver client) throws LogInException {
        user = usersRepository.getOne(user.getUsername()).get();
        IObserver localClient = loggedClients.remove(user.getId());
        if (localClient == null) {
            throw new LogInException("User is not logged in");
        }
    }

    public void logInUser(User user, IObserver client) throws LogInException {
        String username = user.getUsername();
        String passwords = user.getPassword();
        if (usersRepository.checkUser(username, passwords)) {
            user = usersRepository.getOne(username).get();
            if (loggedClients.get(user.getId()) != null)
                throw new LogInException("User already logged In");
            loggedClients.put(user.getId(), client);
        } else {
            throw new LogInException("Incorrect username and password combination");
        }
    }

    public synchronized List<Flight> getAllAvailableFlights() {
        return flightsRepository.getAll().stream().filter(flight -> {
            return flight.getAvailableSeats() > 0;
        }).toList();
    }

    public synchronized List<Flight> getAvailableFilteredFlights(String destination, LocalDate departureDate) {
        FlightFilter filter;
        if (destination.isEmpty()) {
            filter = new FlightFilter(departureDate);
        } else {
            filter = new FlightFilter(destination, departureDate);
        }
        return flightsRepository.getFiltered(filter).stream().filter(flight -> {
            return flight.getAvailableSeats() > 0;
        }).toList();
    }

    public synchronized void buyTicket(int flightId, String clientName, String tourists, String address, int noSeats) throws AppException {
        Optional<Flight> opFlight = flightsRepository.getOne(flightId);
        if (opFlight.isEmpty()) {
            throw new AppException("This flight no longer exists!");
        }
        Flight flight = opFlight.get();
        if (flight.getAvailableSeats() <= 0) {
            throw new AppException("This flight is full!");
        }
        List<String> touristsList = Arrays.stream(tourists.split(";")).toList();
        Ticket ticket = new Ticket(clientName, flight.getId(), address, noSeats);
        ticket.setFlightId(flight.getId());
        ticket.addTourists(touristsList);
        flight.setAvailableSeats(flight.getAvailableSeats() - noSeats);
        if (!flightValidator.validate(flight)) {
            throw new AppException("Not enough seats available!");
        }
        if (!ticketValidator.validate(ticket)) {
            throw new AppException("Incorrect data");
        }
        flightsRepository.updateFlight(flight);
        ticketRepository.addOne(ticket);
        notifyTicketBought();
    }


    private void notifyTicketBought(){
        List<Flight> flights = this.getAllAvailableFlights();
        for (IObserver client:loggedClients.values()){
            client.ticketBought(flights);
        }
    }

}
