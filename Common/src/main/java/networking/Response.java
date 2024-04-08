package networking;


import domain.Flight;
import domain.User;

import java.io.Serializable;
import java.util.Arrays;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private User user;
    private Flight[] flights;

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                ", flights=" + Arrays.toString(flights) +
                '}';
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight[] getFlights() {
        return flights;
    }

    public void setFlights(Flight[] flights) {
        this.flights = flights;
    }

    public Response() {
    }
}
