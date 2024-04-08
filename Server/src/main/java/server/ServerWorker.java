package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.DTOs.FlightFilter;
import domain.DTOs.TicketDTO;
import domain.Flight;
import domain.User;
import exceptions.AppException;
import exceptions.LogInException;
import networking.*;
import service.IService;
import utils.IObserver;
import utils.LocalDateAdapter;
import utils.LocalTimeAdapter;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class ServerWorker implements Runnable, IObserver {

    private IService server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;

    public ServerWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()).create();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Gson gsonFormatter;
    private volatile boolean connected;

    @Override
    public void run() {
        while (connected) {
            try {
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                System.out.println("Recieved request" + request.toString());
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | AppException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private void sendResponse(Response response) {
        String responseLine=gsonFormatter.toJson(response);
        System.out.println("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    private Response okResponse = jsonUtils.createOkResponse();

    private Response handleRequest(Request request) throws AppException{
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            User user = request.getUser();
            try {
                server.logInUser(user, this);
                return okResponse;
            } catch (LogInException e) {
                connected = false;
                return jsonUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.LOGOUT) {
            User user = request.getUser();
            try {
                server.logOut(user, this);
                connected = false;
                return okResponse;
            } catch (LogInException e) {
                return jsonUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.GET_FLIGHTS) {
            Flight[] flights = server.getAllAvailableFlights().toArray(new Flight[0]);
            return jsonUtils.createGetFlightsResponse(flights);
        }
        if(request.getType() == RequestType.GET_FILTERED_FLIGHTS){
            FlightFilter filter = request.getFilter();
            String destination = filter.getDestination().get();
            LocalDate departureDate = filter.getDepartureDate().get();
            Flight[] flights = server.getAvailableFilteredFlights(destination,departureDate).toArray(new Flight[0]);
            return jsonUtils.createGetFlightsResponse(flights);
        }
        if(request.getType()==RequestType.BUY_TICKET){
            TicketDTO ticket = request.getTicket();
            try {
                server.buyTicket(ticket.getFlightId(),ticket.getClientName(), ticket.getTourists(), ticket.getAddress(), ticket.getNoSeats());
                return okResponse;
            } catch (AppException e) {
                return jsonUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public void ticketBought(List<Flight> flights) {
        Response response = new Response();
        response.setType(ResponseType.TICKET_BOUGHT);
        response.setFlights(flights.toArray(new Flight[0]));
        sendResponse(response);
    }
}
