package com.emrej.flightmeal.controller;

import com.emrej.flightmeal.dao.FlightDao;
import com.emrej.flightmeal.exception.FlightExistsException;
import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.repository.FlightRepository;
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

    private FlightRepository flightRepository;
    private FlightService flightService;

    @Autowired
    public FlightController(FlightRepository flightRepository, FlightService flightService) {
        this.flightRepository = flightRepository;
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
        LOGGER.info("Get flight info called..");
        try {
            FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(stringToDate(flightDepartureDate), flightNumber);
            LOGGER.info("Flight retrieved from DB");
            return ResponseEntity.ok(flight);
        } catch (Exception e) {
            LOGGER.error("Error during get flight", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity getAllFlights() {
        LOGGER.info("Get all flights called..");
        try {
            Iterable<FlightDao> flights = flightRepository.findAll();
            LOGGER.info("All flights retrieved from DB");
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            LOGGER.error("Error during get all flights", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
