package server;

import domain.DTOs.FlightFilter;
import domain.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.FlightsRepository;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/flights")
public class FlightController {
    @Autowired
    private FlightsRepository flightsRepository;

    @RequestMapping( method=RequestMethod.GET)
    public List<Flight> getAllFlights() {
        return flightsRepository.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable int id){
        System.out.println("Get by id "+id);
        Optional<Flight> opt=flightsRepository.getOne(id);
        if (opt.isEmpty())
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Flight>(opt.get(), HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.PUT)
    public Flight update(@RequestBody Flight flight){
        flightsRepository.updateFlight(flight);
        return flight;
    }
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Flight> delete(@PathVariable int id){
        Flight response = flightsRepository.delete(id);
        return new ResponseEntity<Flight>(HttpStatus.OK);
    }

    @RequestMapping(method =  RequestMethod.POST)
    public int save(@RequestBody Flight flight){
        return flightsRepository.save(flight);
    }

    @RequestMapping(value = "/filter" ,method = RequestMethod.GET)
    public List<Flight> getFilteredFlights(@RequestBody FlightFilter filter){
        return flightsRepository.getFiltered(filter);
    }
}
