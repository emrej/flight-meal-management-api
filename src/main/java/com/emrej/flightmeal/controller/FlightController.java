package com.emrej.flightmeal.controller;

import com.emrej.flightmeal.dao.FlightDao;
import com.emrej.flightmeal.exception.FlightExistsException;
import com.emrej.flightmeal.exception.FlightNotFoundException;
import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.model.Meals;
import com.emrej.flightmeal.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.emrej.flightmeal.util.DateConverter.dateToString;
import static com.emrej.flightmeal.util.DateConverter.stringToDate;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/flight")
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Add a flight into the DB with flight number and flight departure date
     * Returns the Flight object with 'created' Http status and with 'location' (Url)
     * @param @Flight flight
     * @return ResponseEntity
     * @throws FlightExistsException
     */
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity addFlight(@RequestBody Flight flight) throws FlightExistsException {
        LOGGER.info("Add flight, controller method..");
        FlightDao flightSaved = flightService.addFlight(flight);
        LOGGER.info("Flight created");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{flightNumber}")
                .path("/{flightDepartureDate}")
                .buildAndExpand(flightSaved.getFlightNumber(), dateToString(flightSaved.getFlightDepartureDate()))
                .toUri();

        LOGGER.info("Location is {}", location.toString());

        return ResponseEntity.created(location).body(Flight.to(flightSaved));
    }

    /**
     * Get a flight information including the meals inside the flight
     * @param flightNumber
     * @param flightDepartureDate
     * @return ResponseEntity
     * @throws ParseException
     * @throws FlightNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{flightNumber}/{flightDepartureDate}")
    public ResponseEntity getFlightInfo(@PathVariable("flightNumber") String flightNumber, @PathVariable("flightDepartureDate") String flightDepartureDate) throws ParseException, FlightNotFoundException {
        LOGGER.info("Get flight info, controller method..");
        FlightDao flight = flightService.getFlight(stringToDate(flightDepartureDate), flightNumber);
        LOGGER.info("Flight retrieved from service");
        return ResponseEntity.ok(Flight.to(flight));
    }

    /**
     * Add meals into a flight
     * @param flightNumber
     * @param flightDepartureDate
     * @param meals
     * @return ResponseEntity
     * @throws ParseException
     * @throws FlightNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{flightNumber}/{flightDepartureDate}/meals")
    public ResponseEntity addMeals(@PathVariable("flightNumber") String flightNumber, @PathVariable("flightDepartureDate") String flightDepartureDate, @RequestBody Meals meals) throws ParseException, FlightNotFoundException {
        LOGGER.info("Add meals, controller method..");
        FlightDao flight = flightService.addMeals(stringToDate(flightDepartureDate), flightNumber, meals);
        LOGGER.info("Meals added to flight");
        return ResponseEntity.accepted().body(Flight.to(flight));
    }

    /**
     * Delete a flight with a flight number and departure date, deletes the associated meals as well
     * @param flightNumber
     * @param flightDepartureDate
     * @return ResponseEntity
     * @throws ParseException
     * @throws FlightNotFoundException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{flightNumber}/{flightDepartureDate}")
    public ResponseEntity deleteFlight(@PathVariable("flightNumber") String flightNumber, @PathVariable("flightDepartureDate") String flightDepartureDate) throws ParseException, FlightNotFoundException {
        LOGGER.info("Delete flight, controller method..");
        flightService.deleteFlight(stringToDate(flightDepartureDate), flightNumber);
        LOGGER.info("Flight deleted");
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all flights recorded in the DB
     * @return ResponseEntity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity getAllFlights() {
        LOGGER.info("Get all flights, controller method..");
        Stream<FlightDao> flightDaoStream = flightService.getAllFlights();
        LOGGER.info("All flights retrieved from DB");
        List<Flight> flights = flightDaoStream.map(Flight::to).collect(Collectors.toList());
        return ResponseEntity.ok(flights);
    }

}
