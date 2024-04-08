package utils;

import domain.Flight;
import java.util.List;

public interface IObserver {
    void ticketBought(List<Flight> flights);
}
