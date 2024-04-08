package networking;

import domain.DTOs.FlightFilter;
import domain.DTOs.TicketDTO;
import domain.Flight;
import domain.User;
import networking.Request;
import networking.RequestType;
import networking.Response;
import networking.ResponseType;

public class jsonUtils {
    public static Response createOkResponse(){
        Response response = new Response();
        response.setType(ResponseType.OK);
        return response;
    }
    public static Response createErrorResponse(String message){
        Response response = new Response();
        response.setType(ResponseType.ERROR);
        response.setErrorMessage(message);
        return response;
    }

    public static Response createGetFlightsResponse(Flight[] flights){
        Response response = new Response();
        response.setType(ResponseType.GET_FlIGHTS);
        response.setFlights(flights);
        return response;
    }

    public static Request createLogInRequest(User user){
        Request request = new Request();
        request.setType(RequestType.LOGIN);
        request.setUser(user);
        return request;
    }

    public static Request createLogOutRequest(User user){
        Request request = new Request();
        request.setType(RequestType.LOGOUT);
        request.setUser(user);
        return request;
    }

    public static Request createGetFlightsRequest(){
        Request request = new Request();
        request.setType(RequestType.GET_FLIGHTS);
        return request;
    }

    public static Request createGetFilteredRequest(FlightFilter filter){
        Request request = new Request();
        request.setType(RequestType.GET_FILTERED_FLIGHTS);
        request.setFilter(filter);
        return request;
    }

    public static Request createBuyTicketRequest(TicketDTO ticket){
        Request request = new Request();
        request.setType(RequestType.BUY_TICKET);
        request.setTicket(ticket);
        return request;
    }
}
