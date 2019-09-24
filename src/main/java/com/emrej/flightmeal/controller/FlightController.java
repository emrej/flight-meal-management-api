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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.net.URI;

import static com.emrej.flightmeal.util.DateConverter.dateToString;
import static com.emrej.flightmeal.util.DateConverter.stringToDate;

@RestController
@RequestMapping(path = "/api/flight")
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity addFlight(@RequestBody Flight flight) {
        LOGGER.info("Add flight, controller method..");
        try {
            FlightDao flightSaved = flightService.addFlight(flight);
            LOGGER.info("Flight created");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{flightNumber}")
                    .path("/{flightDepartureDate}")
                    .buildAndExpand(flightSaved.getFlightNumber(), dateToString(flightSaved.getFlightDepartureDate()))
                    .toUri();

            LOGGER.info("Location is {}", location.toString());

            return ResponseEntity.created(location).body(flightSaved);

        } catch (FlightExistsException fe) {
            LOGGER.error("Flight for this date already exists {} {}", flight.getFlightNumber(), flight.getFlightDepartureDate());
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error("Error during add flight", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{flightNumber}/{flightDepartureDate}")
    public ResponseEntity getFlightInfo(@PathVariable("flightNumber") String flightNumber, @PathVariable("flightDepartureDate") String flightDepartureDate) {
        LOGGER.info("Get flight info, controller method..");
        try {
            FlightDao flight = flightService.getFlight(stringToDate(flightDepartureDate), flightNumber);
            LOGGER.info("Flight retrieved from service");
            return ResponseEntity.ok(flight);
        } catch (Exception e) {
            LOGGER.error("Error during get flight info", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{flightNumber}/{flightDepartureDate}/meals")
    public ResponseEntity addMeals(@PathVariable("flightNumber") String flightNumber, @PathVariable("flightDepartureDate") String flightDepartureDate, @RequestBody Meals meals) {
        LOGGER.info("Add meals, controller method..");
        try {
            FlightDao flight = flightService.addMeals(stringToDate(flightDepartureDate), flightNumber, meals);
            LOGGER.info("Meals added to flight");
            return ResponseEntity.accepted().body(flight);
        } catch (FlightNotFoundException fe) {
            LOGGER.error("Flight for this date not found {} {}", flightNumber, flightDepartureDate);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Error during add meals", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity getAllFlights() {
        LOGGER.info("Get all flights, controller method..");
        try {
            Iterable<FlightDao> flights = flightService.getAllFlights();
            LOGGER.info("All flights retrieved from DB");
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            LOGGER.error("Error during get all flights", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
