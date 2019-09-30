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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class FlightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    private FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Adds a flight into the DB and returns back that added flight DAO
     * @param newFlight
     * @return FlightDao
     * @throws FlightExistsException
     */
    public FlightDao addFlight(Flight newFlight) throws FlightExistsException {
        LOGGER.info("Add flight called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(newFlight.getFlightDepartureDate(), newFlight.getFlightNumber());
        if (null == flight) {
            flight = flightRepository.save(new FlightDao(newFlight.getFlightDepartureDate(), newFlight.getFlightNumber()));
            LOGGER.info("Flight saved");
            return flight;
        } else {
            LOGGER.warn("Flight for this date already exists. {} {}", newFlight.getFlightNumber(), newFlight.getFlightDepartureDate());
            throw new FlightExistsException("Flight for this date already exists.");
        }
    }

    /**
     * Adds meals into a flight, saves the flight with the meals and returns back the Flight DAO (which includes meals)
     * @param flightDepartureDate
     * @param flightNumber
     * @param meals
     * @return FlightDao
     * @throws FlightNotFoundException
     */
    public FlightDao addMeals(Date flightDepartureDate, String flightNumber, Meals meals) throws FlightNotFoundException {
        LOGGER.info("Add meals called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(flightDepartureDate, flightNumber);
        if (null != flight) {
            flight.addMeals(meals.from());
            flight = flightRepository.save(flight);
            LOGGER.info("Flight with meals saved");
            return flight;
        } else {
            LOGGER.warn("There is no flight for this date. {} {}", flightNumber, flightDepartureDate);
            throw new FlightNotFoundException("There is no flight for this date.");
        }
    }

    /**
     * Gets a flight from DB and returns as the Flight DAO
     * @param flightDepartureDate
     * @param flightNumber
     * @return FlightDao
     * @throws FlightNotFoundException
     */
    public FlightDao getFlight(Date flightDepartureDate, String flightNumber) throws FlightNotFoundException {
        LOGGER.info("Get flight called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(flightDepartureDate, flightNumber);
        if (null == flight) {
            LOGGER.warn("There is no flight for this date. {} {}", flightNumber, flightDepartureDate);
            throw new FlightNotFoundException("There is no flight for this date.");
        }
        LOGGER.info("Flight retrieved from DB");
        return flight;
    }

    /**
     * Deletes a flight from DB; if not found, throws an exception
     * @param flightDepartureDate
     * @param flightNumber
     * @throws FlightNotFoundException
     */
    public void deleteFlight(Date flightDepartureDate, String flightNumber) throws FlightNotFoundException {
        LOGGER.info("Delete flight called..");
        FlightDao flight = flightRepository.findByFlightDepartureDateAndFlightNumber(flightDepartureDate, flightNumber);
        if (null != flight) {
            flightRepository.delete(flight);
            LOGGER.info("Flight deleted");
        } else {
            LOGGER.warn("There is no flight for this date. {} {}", flightNumber, flightDepartureDate);
            throw new FlightNotFoundException("There is no flight for this date.");
        }
    }

    /**
     * Gets all flights stored in DB and returns them as a stream
     * @return Stream<FlightDao>
     */
    public Stream<FlightDao> getAllFlights() {
        LOGGER.info("Get all flights called..");
        Iterable<FlightDao> flights = flightRepository.findAll();
        LOGGER.info("All flights retrieved from DB");
        return StreamSupport.stream(flights.spliterator(), false);
    }
}
