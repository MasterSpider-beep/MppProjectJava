package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.DTOs.FlightFilter;
import domain.DTOs.TicketDTO;
import domain.Flight;
import domain.User;
import exceptions.AppException;
import exceptions.LogInException;
import networking.Request;
import networking.Response;
import networking.ResponseType;
import networking.jsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.IService;
import utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IService {

    private String host;
    private int port;
    private IObserver client;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    public ServiceProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }


    private void initializeConnection() throws AppException {
        try {
            gsonFormatter = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).
                    registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()).serializeNulls().
                    create();
            //gsonFormatter = new Gson();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    @Override
    public void logInUser(User user, IObserver client) throws LogInException {
        try {
            initializeConnection();
            Request request = jsonUtils.createLogInRequest(user);
            sendRequest(request);
            Response response = readResponse();
            if(response.getType()==ResponseType.OK){
                this.client = client;
                return;
            }
            if(response.getType() == ResponseType.ERROR){
                closeConnection();
                throw new LogInException(response.getErrorMessage());
            }
        } catch (AppException e) {
            throw new LogInException(e.getMessage());
        }
    }

    private void handleUpdate(Response response) {
        List<Flight> flights = Arrays.asList(response.getFlights());
        client.ticketBought(flights);
    }

    private static final Logger logger = LogManager.getLogger();
    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    logger.traceEntry("Trying to read line...");
                    System.out.println("Trying to read line...");
                    String responseLine = input.readLine();
                    logger.traceExit("Response received: {}", responseLine);
                    //System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (response.getType() == ResponseType.TICKET_BOUGHT) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    @Override
    public void logOut(User user, IObserver client) throws LogInException {
        Request request = jsonUtils.createLogOutRequest(user);
        try {
            sendRequest(request);
            Response response = readResponse();
            closeConnection();
        } catch (AppException e) {
            throw new LogInException(e.getMessage());
        }
    }

    @Override
    public List<Flight> getAllAvailableFlights() throws AppException {
        Request request = jsonUtils.createGetFlightsRequest();
        sendRequest(request);
        Response response = readResponse();
        return Arrays.stream(response.getFlights()).toList();
    }

    @Override
    public List<Flight> getAvailableFilteredFlights(String destination, LocalDate departureDate) throws AppException {
        Request request = jsonUtils.createGetFilteredRequest(new FlightFilter(destination,departureDate));
        sendRequest(request);
        Response response = readResponse();
        return Arrays.asList(response.getFlights());
    }

    @Override
    public void buyTicket(int flightId, String clientName, String tourists, String address, int noSeats) throws AppException {
        TicketDTO ticket = new TicketDTO(flightId,clientName,tourists,address,noSeats);
        Request request = jsonUtils.createBuyTicketRequest(ticket);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType()==ResponseType.ERROR){
            throw new AppException(response.getErrorMessage());
        }
    }

    private void sendRequest(Request request) throws AppException {
        String reqLine = gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new AppException("Error sending object " + e);
        }
    }
    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
