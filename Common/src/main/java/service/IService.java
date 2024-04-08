package service;

import domain.Flight;
import domain.User;
import exceptions.AppException;
import exceptions.LogInException;
import utils.IObserver;

import java.time.LocalDate;
import java.util.List;

public interface IService {
    public void logInUser(User user, IObserver client) throws LogInException;
    public void logOut(User user, IObserver client) throws LogInException;

    public List<Flight> getAllAvailableFlights() throws AppException;

    public List<Flight> getAvailableFilteredFlights(String destination, LocalDate departureDate) throws AppException;

    public void buyTicket(int flightId, String clientName, String tourists, String address, int noSeats) throws AppException;

}