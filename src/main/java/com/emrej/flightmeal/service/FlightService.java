package com.emrej.flightmeal.service;

import com.emrej.flightmeal.dao.FlightDao;
import com.emrej.flightmeal.exception.FlightExistsException;
import com.emrej.flightmeal.exception.FlightNotFoundException;
import com.emrej.flightmeal.model.Flight;
import com.emrej.flightmeal.model.Meals;
import com.emrej.flightmeal.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FlightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    private FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightDao addFlight(Flight newFlight) throws FlightExistsException {
        LOGGER.info("Add flight called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(newFlight.getFlightDepartureDate(), newFlight.getFlightNumber());
        if (null == flight) {
            flight = flightRepository.save(new FlightDao(newFlight.getFlightDepartureDate(), newFlight.getFlightNumber()));
            LOGGER.info("Flight saved");
            return flight;
        } else {
            LOGGER.warn("Flight for this date already exists.");
            throw new FlightExistsException("Flight for this date already exists.");
        }
    }

    public FlightDao addMeals(Date flightDepartureDate, String flightNumber, Meals meals) throws FlightNotFoundException {
        LOGGER.info("Add meals called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(flightDepartureDate, flightNumber);
        if (null != flight) {
            flight.addMeals(meals.from());
            flight = flightRepository.save(flight);
            LOGGER.info("Flight with meals saved");
            return flight;
        } else {
            LOGGER.warn("There is no flight for this date.");
            throw new FlightNotFoundException("There is no flight for this date.");
        }
    }

    public FlightDao getFlight(Date flightDepartureDate, String flightNumber) {
        LOGGER.info("Get flight called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(flightDepartureDate, flightNumber);
        LOGGER.info("Flight retrieved from DB");
        return flight;
    }

    public Iterable<FlightDao> getAllFlights() {
        LOGGER.info("Get all flights called..");
        Iterable<FlightDao> flights = flightRepository.findAll();
        LOGGER.info("All flights retrieved from DB");
        return flights;
    }
}
